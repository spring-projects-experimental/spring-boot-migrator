/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.jee.jaxrs.actions;

import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.*;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@NoArgsConstructor
@SuperBuilder
public class ConvertJaxRsAnnotations extends AbstractAction {

    public static final Pattern JAXRS_ANNOTATION_PATTERN = Pattern.compile("javax\\.ws\\.rs\\..*");
    public static final Pattern SPRING_ANNOTATION_PATTERN = Pattern.compile("org\\.springframework\\.web\\.bind\\..*");

    @Override
    public void apply(ProjectContext context) {
        for (JavaSource js : context.getProjectJavaSources().list()) {
            for (Type t : js.getTypes()) {
                if (t.hasAnnotation("javax.ws.rs.Path")) {
                    transform(t);
                }
            }
        }
    }

    private void transform(Type type) {
        transformTypeAnnotations(type);
        transformMethodAnnotations(type);
    }

    private void transformMethodAnnotations(Type type) {
        type.getMethods().stream()
                .filter(this::isJaxRsMethod)
                .forEach(this::convertJaxRsMethodToSpringMvc);
    }

    void convertJaxRsMethodToSpringMvc(Method method) {
        Map<String, Expression> attrs = new LinkedHashMap<>();
        Set<String> methods = new LinkedHashSet<>();
        var annotations = method.getAnnotations();

        // Add @RequestBody over the first non-annotated parameter without other jax-rs annotations
        method.getParams().stream()
                .filter(p -> !p.containsAnnotation(JAXRS_ANNOTATION_PATTERN))
                .filter(p -> !p.containsAnnotation(SPRING_ANNOTATION_PATTERN))
                .findFirst()
                .ifPresent(p -> p.addAnnotation("@RequestBody", "org.springframework.web.bind.annotation.RequestBody"));

        for (Annotation a : annotations) {
            if (a == null) {
                continue;
            }
            String fullyQualifiedName = a.getFullyQualifiedName();
            if (fullyQualifiedName != null) {
                switch (fullyQualifiedName) {
                    case "javax.ws.rs.Path":
                        attrs.put("value", a.getAttribute("value"));
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.Consumes":
                        attrs.put("consumes", a.getAttribute("value"));
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.Produces":
                        attrs.put("produces", a.getAttribute("value"));
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.POST":
                        methods.add("POST");
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.GET":
                        methods.add("GET");
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.PUT":
                        methods.add("PUT");
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.DELETE":
                        methods.add("DELETE");
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.HEAD":
                        methods.add("HEAD");
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.PATCH":
                        methods.add("PATCH");
                        method.removeAnnotation(a);
                        break;
                    case "javax.ws.rs.TRACE":
                        methods.add("TRACE");
                        method.removeAnnotation(a);
                        break;
                    default:
                }
            }
        }

        if (method.getAnnotations().size() < annotations.size()) {
            StringBuilder sb = new StringBuilder("@RequestMapping");
            boolean parametersPresent = !(attrs.isEmpty() && methods.isEmpty());
            if (parametersPresent) {
                sb.append("(");
            }

            sb.append(attrs.entrySet().stream()
                              .map(e -> e.getKey() + " = " + e.getValue().print())
                              .collect(Collectors.joining(", ")));

            if (!methods.isEmpty()) {
                if(!attrs.entrySet().isEmpty()) {
                    sb.append(", ");
                }
                if (methods.size() == 1) {
                    sb.append("method = RequestMethod." + methods.iterator().next());
                } else {
                    sb.append("method = {");
                    sb.append(methods.stream().map(m -> "RequestMethod." + m).collect(Collectors.joining(", ")));
                    sb.append("}");
                }
            }
            if (parametersPresent) {
                sb.append(")");
            }

            Set<String> typeStubs = Set.of(
                    """
                    package org.springframework.web.bind.annotation;
                    import java.lang.annotation.Documented;
                    import java.lang.annotation.ElementType;
                    import java.lang.annotation.Retention;
                    import java.lang.annotation.RetentionPolicy;
                    import java.lang.annotation.Target;
                    import org.springframework.aot.hint.annotation.Reflective;
                    import org.springframework.core.annotation.AliasFor;
                    @Target({ElementType.TYPE, ElementType.METHOD})
                    @Retention(RetentionPolicy.RUNTIME)
                    @Documented
                    @Mapping
                    @Reflective(ControllerMappingReflectiveProcessor.class)
                    public @interface RequestMapping {
                        String name() default "";
                        @AliasFor("path")
                        String[] value() default {};
                        @AliasFor("value")
                        String[] path() default {};
                        RequestMethod[] method() default {};
                        String[] params() default {};
                        String[] headers() default {};
                        String[] consumes() default {};
                        String[] produces() default {};
                                        
                    }
                    """,
                    """
                    package org.springframework.web.bind.annotation;
                                        
                    import org.springframework.http.HttpMethod;
                    import org.springframework.lang.Nullable;
                    import org.springframework.util.Assert;
                    public enum RequestMethod {
                                        
                        GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
                                        
                        @Nullable
                        public static RequestMethod resolve(String method) {
                            Assert.notNull(method, "Method must not be null");
                            return switch (method) {
                                case "GET" -> GET;
                                case "HEAD" -> HEAD;
                                case "POST" -> POST;
                                case "PUT" -> PUT;
                                case "PATCH" -> PATCH;
                                case "DELETE" -> DELETE;
                                case "OPTIONS" -> OPTIONS;
                                case "TRACE" -> TRACE;
                                default -> null;
                            };
                        }
                        @Nullable
                        public static RequestMethod resolve(HttpMethod httpMethod) {
                            Assert.notNull(httpMethod, "HttpMethod must not be null");
                            return resolve(httpMethod.name());
                        }
                        public HttpMethod asHttpMethod() {
                            return switch (this) {
                                case GET -> HttpMethod.GET;
                                case HEAD -> HttpMethod.HEAD;
                                case POST -> HttpMethod.POST;
                                case PUT -> HttpMethod.PUT;
                                case PATCH -> HttpMethod.PATCH;
                                case DELETE -> HttpMethod.DELETE;
                                case OPTIONS -> HttpMethod.OPTIONS;
                                case TRACE -> HttpMethod.TRACE;
                            };
                        }
                    }
                    """
            );

            method.addAnnotation(sb.toString(), "org.springframework.web.bind.annotation.RequestMapping", typeStubs, "org.springframework.web.bind.annotation.RequestMethod");

        }
    }

    private boolean isJaxRsMethod(Method method) {
        return method.containsAnnotation(JAXRS_ANNOTATION_PATTERN);
    }

    private void transformTypeAnnotations(Type type) {
        List<Annotation> annotations = type.getAnnotations();
        Optional<Annotation> found = annotations.stream().filter(a -> "javax.ws.rs.Path".equals(a.getFullyQualifiedName())).findFirst();
        if (found.isPresent()) {
            type.removeAnnotation(found.get());
            String fqName = "org.springframework.web.bind.annotation.RestController";
            String snippet = "@" + fqName.substring(fqName.lastIndexOf('.') + 1);
            Set<String> stubs = Set.of("""
                    package org.springframework.web.bind.annotation;
                                        
                    import java.lang.annotation.Documented;
                    import java.lang.annotation.ElementType;
                    import java.lang.annotation.Retention;
                    import java.lang.annotation.RetentionPolicy;
                    import java.lang.annotation.Target;
                                        
                    import org.springframework.core.annotation.AliasFor;
                    import org.springframework.stereotype.Controller;
                    @Target(ElementType.TYPE)
                    @Retention(RetentionPolicy.RUNTIME)
                    @Documented
                    @Controller
                    @ResponseBody
                    public @interface RestController {
                    	@AliasFor(annotation = Controller.class)
                    	String value() default "";
                    }
                    """);
            type.addAnnotation(snippet, "org.springframework.web.bind.annotation.RestController", stubs);
            Map<String, Expression> attributes = new LinkedHashMap<>(found.get().getAttributes());
            for (Annotation a : annotations) {
                if (a == null) {
                    continue;
                }
                String fullyQualifiedName = a.getFullyQualifiedName();
                if (fullyQualifiedName != null) {
                    switch (fullyQualifiedName) {
                        case "javax.ws.rs.Consumes" -> {
                            attributes.put("consumes", a.getAttribute("value"));
                            type.removeAnnotation(a);
                        }
                        case "javax.ws.rs.Produces" -> {
                            attributes.put("produces", a.getAttribute("value"));
                            type.removeAnnotation(a);
                        }
                        default -> {
                        }
                    }
                }
            }
            String rmAttrs = attributes.entrySet().stream().map(e -> e.getKey() + " = " + e.getValue().print()).collect(Collectors.joining(", "));
            type.addAnnotation("@RequestMapping(" + rmAttrs + ")", "org.springframework.web.bind.annotation.RequestMapping", Set.of("""
                    package org.springframework.web.bind.annotation;
                                        
                    import java.lang.annotation.Documented;
                    import java.lang.annotation.ElementType;
                    import java.lang.annotation.Retention;
                    import java.lang.annotation.RetentionPolicy;
                    import java.lang.annotation.Target;
                                        
                    import org.springframework.aot.hint.annotation.Reflective;
                    import org.springframework.core.annotation.AliasFor;
                                        
                    @Target({ElementType.TYPE, ElementType.METHOD})
                    @Retention(RetentionPolicy.RUNTIME)
                    @Documented
                    @Mapping
                    @Reflective(ControllerMappingReflectiveProcessor.class)
                    public @interface RequestMapping {
                                        
                    	String name() default "";
                                        
                    	@AliasFor("path")
                    	String[] value() default {};
                                        
                    	@AliasFor("value")
                    	String[] path() default {};
                                        
                    	RequestMethod[] method() default {};
                                        
                    	String[] params() default {};
                                        
                    	String[] headers() default {};
                            
                    	String[] consumes() default {};
                                        
                    	String[] produces() default {};
                                        
                    }
                    """));
        }
    }
}
