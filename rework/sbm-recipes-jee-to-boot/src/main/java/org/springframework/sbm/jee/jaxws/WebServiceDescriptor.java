/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.jee.jaxws;

import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.java.api.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Getter;
import org.openrewrite.Recipe;
import org.openrewrite.java.OrderImports;
import org.openrewrite.java.RemoveAnnotation;
import org.openrewrite.java.format.AutoFormat;
import org.openrewrite.xml.XPathMatcher;
import org.openrewrite.xml.tree.Xml;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alex Boyko
 */
@Getter
class WebServiceDescriptor {

    public static final String WEB_SERVICE_ANNOTATION = "javax.jws.WebService";
    private static final String WEB_METHOD_ANNOTATION = "javax.jws.WebMethod";
    private static final String WEB_PARAM_ANNOTATION = "javax.jws.WebParam";
    private static final String WEB_RESULT_ANNOTATION = "javax.jws.WebResult";
    private static final String ONE_WAY_ANNOTATION = "javax.jws.Oneway";

    private static final String NSURI_CONSTANT_NAME = "NAMESPACE_URI";
    public static final String HTTP_SCHEMAS_XML_WSDL = "http://schemas.xmlsoap.org/wsdl/";
    public static final String HTTP_SCHEMAS_XML_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
    public static final String WS = "ws";

    private final Type typeAnnotatedAsWebService;

    private final Type endpointInterface;

    private final Xml.Document wsdl;

    private final Configuration configuration;

    private String nsUri;

    private String packageName;

    private Type serviceType;

    private String serviceFieldName;

    private String pathContext;

    private String wsdlDefBeanName;

    public WebServiceDescriptor(Type typeAnnotatedAsWebService, Type effectiveInterface, Xml.Document wsdl, Configuration configuration) {
        this.typeAnnotatedAsWebService = typeAnnotatedAsWebService;
        this.endpointInterface = effectiveInterface;
        this.wsdl = wsdl;
        this.configuration = configuration;
        init();
    }

    private void init() {
        nsUri = computeNsUri();
        packageName = computePackageName();
        serviceType = endpointInterface == null ? typeAnnotatedAsWebService : endpointInterface;
        if (serviceType != null) {
            serviceFieldName = StringUtils.uncapitalize(serviceType.getSimpleName());
        }
        initPathContext();
    }

    private String computeNsUri() {
        if (wsdl != null) {
            return wsdl.getRoot().getAttributes().stream().filter(a -> "targetNamespace".equals(a.getKeyAsString())).map(a -> a.getValueAsString()).findFirst().orElse(null);
        } else {
            return Stream.concat(endpointInterface == null ? Stream.empty() : endpointInterface.getAnnotations().stream(), typeAnnotatedAsWebService.getAnnotations().stream())
                    .filter(a -> WEB_SERVICE_ANNOTATION.equals(a.getFullyQualifiedName()))
                    .map(a -> a.getAttribute("targetNamespace"))
                    .filter(Objects::nonNull)
                    .map(e -> e.getAssignmentRightSide())
                    .map(e -> e.print())
                    .map(s -> s.replace("\"", ""))
                    .findFirst()
                    .orElse(null);
        }
    }

    private String computePackageName() {
        if (nsUri != null) {
            URI uri = URI.create(nsUri);
            String host = uri.getHost();
            List<String> pkgNameTokens = new ArrayList<>();
            String[] tokens = host.split("\\.");
            for (int i = tokens.length - 1; i >= 0; i--) {
                pkgNameTokens.add(tokens[i]);
            }
            for (String t : uri.getPath().split("/")) {
                if (!t.isEmpty()) {
                    pkgNameTokens.add(t);
                }
            }
            return String.join(".", pkgNameTokens);
        }
        return "com.sbm.generated";
    }


    public JavaSource generateEndpoint(Module module) {
        List<WebServiceOperation> ops = (endpointInterface == null ? typeAnnotatedAsWebService.getMethods().stream().filter(m -> m.getVisibility() == Visibility.PUBLIC) : endpointInterface.getMethods().stream())
                .map(this::createWebServiceOperation)
                .collect(Collectors.toList());

        JavaSource javaSource = generateEndpointSource(module, ops);

        javaSource.apply(new OrderImports(false).doNext(new AutoFormat()));

        return javaSource;
    }

    private WebServiceOperation createWebServiceOperation(Method m) {
        WebServiceOperation op = new WebServiceOperation();

        String name = m.getAnnotation(WEB_METHOD_ANNOTATION)
                .map(a -> a.getAttribute("operationName"))
                .filter(Objects::nonNull)
                .map(e -> e.getAssignmentRightSide())
                .map(e -> e.print())
                .map(s -> s.replace("\"", ""))
                .orElse(m.getName());
        op.setName(name);
        op.setServiceMethodName(m.getName());


        WebServiceOperation.Type output = null;
        if (!m.getAnnotation(ONE_WAY_ANNOTATION).isPresent()) {
            output = new WebServiceOperation.Type();
            output.setPackageName(packageName);
            output.setSimpleName(StringUtils.capitalize(op.getName()) + "Response");
            output.setFields(new String[]{
                    m.getAnnotation(WEB_RESULT_ANNOTATION)
                            .map(a -> a.getAttribute("name"))
                            .filter(Objects::nonNull)
                            .map(e -> e.getAssignmentRightSide())
                            .map(e -> e.print())
                            .map(s -> s.replace("\"", ""))
                            .orElse("return")
            });
        }
        op.setOutput(output);

        WebServiceOperation.Type input = new WebServiceOperation.Type();
        input.setPackageName(packageName);
        input.setSimpleName(StringUtils.capitalize(op.getName()));
        String[] fields = new String[m.getParams().size()];
        for (int i = 0; i < m.getParams().size(); i++) {
            MethodParam param = m.getParams().get(i);
            fields[i] = param.getAnnotations().stream()
                    .filter(a -> WEB_PARAM_ANNOTATION.equals(a.getFullyQualifiedName()))
                    .map(a -> a.getAttribute("name"))
                    .filter(Objects::nonNull)
                    .map(e -> e.getAssignmentRightSide())
                    .map(e -> e.print())
                    .map(s -> s.replace("\"", ""))
                    .findFirst()
                    .orElse("arg" + i);
        }
        input.setFields(fields);

        op.setInput(input);

        return op;
    }

    private JavaSource generateEndpointSource(Module module, List<WebServiceOperation> ops) {
        JavaSource clazzSource = module.getMainJavaSourceSet()
                .stream()
                .filter(js -> js.getTypes().stream().filter(t -> typeAnnotatedAsWebService.getFullyQualifiedName().equals(t.getFullyQualifiedName())).findFirst().isPresent())
                .findFirst()
                .get();

        String packageName = clazzSource.getPackageName();
        Path sourceFolder = clazzSource.getSourceFolder();
        if (sourceFolder == null) {
            JavaSourceLocation location = module.getMainJavaSourceSet().getJavaSourceLocation();
            sourceFolder = location.getSourceFolder();
            packageName = location.getPackageName();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("packageName", packageName);
        params.put("className", serviceType.getSimpleName() + "Endpoint");
        params.put("serviceFqName", serviceType.getFullyQualifiedName());
        params.put("serviceSimpleName", serviceType.getSimpleName());
        params.put("serviceFieldName", serviceFieldName);
        params.put("nsUriConstantName", NSURI_CONSTANT_NAME);
        params.put("nsUri", nsUri);

        params.put("operations", ops.stream().map(op -> op.createSnippet(serviceFieldName, NSURI_CONSTANT_NAME)).collect(Collectors.toList()));
        params.put("imports", ops.stream().flatMap(op -> Arrays.stream(op.getImports())).collect(Collectors.toSet()));

        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("jaxws-endpoint.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String src = writer.toString();
        return module.getMainJavaSourceSet().addJavaSource(module.getProjectRootDirectory(), sourceFolder, src, packageName);
    }

    public void removeJeeAnnotations() {
        Recipe recipe = new RemoveAnnotation(WEB_SERVICE_ANNOTATION)
                .doNext(new RemoveAnnotation(WEB_METHOD_ANNOTATION))
                .doNext(new RemoveAnnotation(WEB_PARAM_ANNOTATION))
                .doNext(new RemoveAnnotation(WEB_RESULT_ANNOTATION))
                .doNext(new RemoveAnnotation(ONE_WAY_ANNOTATION));

        typeAnnotatedAsWebService.apply(recipe);
        if (endpointInterface != null) {
            endpointInterface.apply(recipe);
        }
    }

    public void initPathContext() {
        if (wsdl != null) {
            String wsdlNsPrefix = XmlUtils.findNsPrefix(wsdl.getRoot(), HTTP_SCHEMAS_XML_WSDL);
            String soapNsPrefix = XmlUtils.findNsPrefix(wsdl.getRoot(), HTTP_SCHEMAS_XML_SOAP);
            if (wsdlNsPrefix != null && soapNsPrefix != null) {
                String xpath = String.format("/%1$s:definitions/%1$s:service/%1$s:port/%2$s:address", wsdlNsPrefix, soapNsPrefix);
                XmlUtils.getFirstTagAttribute(wsdl, new XPathMatcher(xpath), "location")
                        .map(URI::create)
                        .map(uri -> Path.of(uri.getPath()))
                        .ifPresent(p -> {
                            pathContext = p.subpath(0, p.getNameCount() - 1).toString();
                            wsdlDefBeanName = p.subpath(p.getNameCount() - 1, p.getNameCount()).toString();
                        });
            }
            if (wsdlNsPrefix == null && soapNsPrefix != null) {
                String xpath = String.format("/definitions/service/port/%1$s:address", soapNsPrefix);
                XmlUtils.getFirstTagAttribute(wsdl, new XPathMatcher(xpath), "location")
                        .map(URI::create)
                        .map(uri -> Path.of(uri.getPath()))
                        .ifPresent(p -> {
                            pathContext = p.subpath(0, p.getNameCount() - 1).toString();
                            wsdlDefBeanName = p.subpath(p.getNameCount() - 1, p.getNameCount()).toString();
                        });
            }

        }
        if (pathContext == null) {
            pathContext = "ws";
            if (endpointInterface != null) {
                wsdlDefBeanName = StringUtils.uncapitalize(endpointInterface.getSimpleName());
                return;
            }
            String clazzName = StringUtils.uncapitalize(typeAnnotatedAsWebService.getSimpleName());
            String postfix = clazzName.substring(clazzName.length() - WS.length());
            if (WS.equalsIgnoreCase(postfix)) {
                wsdlDefBeanName = clazzName.substring(0, clazzName.length() - WS.length());
            } else {
                wsdlDefBeanName = clazzName + WS;
            }
        }
    }

    public boolean isPojoCodePresent(Module module) {
        return module.getMainJavaSourceSet().list().stream()
                .filter(js -> packageName.equals(js.getPackageName()))
                .findFirst()
                .isPresent();
    }
}
