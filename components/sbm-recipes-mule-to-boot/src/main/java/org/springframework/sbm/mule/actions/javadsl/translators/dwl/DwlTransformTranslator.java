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
package org.springframework.sbm.mule.actions.javadsl.translators.dwl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Version;
import freemarker.template.Template;
import lombok.Setter;
import org.mulesoft.schema.mule.ee.dw.TransformMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.java.util.Helper;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class DwlTransformTranslator implements MuleComponentToSpringIntegrationDslTranslator<TransformMessageType> {
    public static final String TRANSFORM_STATEMENT_CONTENT = ".transform($CLASSNAME::transform)";
    public static final String externalPackageName = "com.example.javadsl";

    @Autowired
    @Setter
    @JsonIgnore
    private Configuration templateConfiguration;

    /* Define the stubs for adding the transformation as a comment to be addressed */
    private static final String externalClassContentPrefixTemplate = "package " + externalPackageName + ";\n\n" +
            "public class $CLASSNAME {\n" +
            "    /*\n" +
            "     * TODO:\n" +
            "     *\n" +
            "     * Please add necessary transformation for below snippet\n";

    private static final String externalClassContentSuffixTemplate = "     * */\n" +
            "    public static $CLASSNAME transform(Object payload) {\n" +
            "\n" +
            "        return new $CLASSNAME();\n" +
            "    }\n" +
            "}";

    /*
     * Define the TriggerMesh specific stubs when enabled. This will capture the transformation, and send it along
     * with the payload to the TriggerMesh Dataweave Transformation Service.
     */
    private static final String triggermeshPayloadHandlerContent = "" +
            ".handle((p, h) -> {\n" +
            "                    TmDwPayload dwPayload = new TmDwPayload();\n" +
            "                    String contentType = \"application/json\";\n" +
            "                    if (h.get(\"contentType\") != null) { contentType = h.get(\"contentType\").toString(); }\n" +
            "                    dwPayload.setId(h.getId().toString());\n" +
            "                    dwPayload.setSourceType(contentType);\n" +
            "                    dwPayload.setSource(h.get(\"http_requestUrl\").toString());\n" +
            "                    dwPayload.setPayload(p.toString());\n" +
            "                    return dwPayload;\n" +
            "                })";

    @Override
    public Class<TransformMessageType> getSupportedMuleType() {
        return TransformMessageType.class;
    }

    @Override
    public DslSnippet translate(
            int id,
            TransformMessageType component,
            QName name,
            MuleConfigurations muleConfigurations,
            String flowName,
            Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap
    ) {
        // Ugly hack to work around an inability to inject a sbm property into the mulesoft parser.
        String isTmTransformationEnabled = System.getProperty("sbm.muleTriggerMeshTransformEnabled");

        if (component.getSetPayload() != null) {
            if (isComponentReferencingAnExternalFile(component)) {
                return formExternalFileBasedDSLSnippet(component);
            }

            if (isTmTransformationEnabled != null && isTmTransformationEnabled.equals("true")) {
                return formTriggerMeshDWLBasedDSLSnippet(component, Helper.sanitizeForBeanMethodName(flowName), id);
            } else {
                return formEmbeddedDWLBasedDSLSnippet(component, Helper.sanitizeForBeanMethodName(flowName), id);
            }
        }

        return noSupportDslSnippet();
    }

    private DslSnippet noSupportDslSnippet() {
        String noSupport = " // FIXME: No support for following DW transformation: <dw:set-property/> <dw:set-session-variable /> <dw:set-variable />";
        return DslSnippet.builder()
                .renderedSnippet(noSupport)
                .build();
    }

    private DslSnippet formEmbeddedDWLBasedDSLSnippet(TransformMessageType component, String flowName, int id) {
        String className = capitalizeFirstLetter(flowName) + "Transform_" + id;

        String dwlContent = component.getSetPayload().getContent().toString();
        String dwlContentCommented = "     * " + dwlContent.replace("\n", "\n     * ") + "\n";
        String externalClassContent =
                replaceClassName(externalClassContentPrefixTemplate, className) +
                        dwlContentCommented +
                        replaceClassName(externalClassContentSuffixTemplate, className);

        return DslSnippet.builder()
                .renderedSnippet(replaceClassName(TRANSFORM_STATEMENT_CONTENT, className))
                .externalClassContent(externalClassContent)
                .build();
    }

    private DslSnippet formTriggerMeshDWLBasedDSLSnippet(TransformMessageType component, String flowName, int id) {
        String className = capitalizeFirstLetter(flowName) + "TransformTM_" + id;
        String dwlSpell = component.getSetPayload().getContent().toString();

        // Locate the output content type based on the spell. If it isn't present, default to
        // application/json
        String outputContentType = getSpellOutputType(dwlSpell);
        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("className", className);
        templateParams.put("outputContentType", outputContentType);
        templateParams.put("dwSpell", sanitizeSpell(dwlSpell));
        templateParams.put("packageName", externalPackageName);

        StringWriter sw  = new StringWriter();
        try {
            // In cases where the template library is not initialized (unit testing)
            if (templateConfiguration == null) {
                templateConfiguration = new Configuration(new Version("2.3.0"));
                templateConfiguration.setTemplateLoader(new FileTemplateLoader(new File("./src/main/resources/templates")));
            }

            Template template = templateConfiguration.getTemplate("triggermesh-dw-transformation-template.ftl");
            template.process(templateParams, sw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String tmTransformationContent = sw.toString();

        // Build the dw payload
        return DslSnippet.builder()
                .renderedSnippet(triggermeshPayloadHandlerContent + "\n" + replaceClassName(TRANSFORM_STATEMENT_CONTENT, className))
                .externalClassContent(tmTransformationContent)
                .build();
    }

    private String getSpellOutputType(String spell) {
        String spellOutputType = "application/json";

        String []spellElements = spell.split(" ");
        for (int i = 0; i < spellElements.length; i++) {
            if (spellElements[i].equals("%output")) {
                spellOutputType = spellElements[i+1].trim();
                break;
            } else if (spellElements[i].equals("---")) {
                break;
            }
        }

        if (spellOutputType.contains(";")) {
            spellOutputType = spellOutputType.split(";")[0];
        }

        return spellOutputType;
    }

    private DslSnippet formExternalFileBasedDSLSnippet(TransformMessageType component) {
        String resource = component.getSetPayload().getResource();
        String className = sanitizeForClassName(resource);
        String content =
                replaceClassName(externalClassContentPrefixTemplate, className)
                        + "     * from file "
                        + resource.replace("classpath:", "")
                        + replaceClassName(externalClassContentSuffixTemplate, className);
        return DslSnippet.builder()
                .renderedSnippet(replaceClassName(TRANSFORM_STATEMENT_CONTENT, className))
                .externalClassContent(content)
                .build();
    }

    public static String sanitizeForClassName(String classNameCandidate) {
        String sanitizedClassName = getFileName(classNameCandidate)
                .replaceAll("[^a-zA-Z0-9]", "");
        return (capitalizeFirstLetter(sanitizedClassName) + "Transform");
    }

    // Remove the leading/trailing spaces, [], ensure the double quote marks are escaped, and swap out the newlines
    private static String sanitizeSpell(String spell) {
        String s = spell.trim();
        if (s.charAt(0) == '[' && s.charAt(s.length() -1) == ']') {
            s = s.substring(1);
            s = s.substring(0, s.length() - 1);
        }
        s = s.replace("\"", "\\\"");
        s = s.replace("\n", "\\n");
        return s;
    }

    private boolean isComponentReferencingAnExternalFile(TransformMessageType component) {
        return component.getSetPayload().getContent().isEmpty();
    }

    private static String getFileName(String path) {
        String[] fileParts = path.replace("classpath:", "").split("\\.");
        String pathWithoutExtension = fileParts.length == 1 ?
                fileParts[0] : fileParts[fileParts.length - 2];
        String[] fileNameParts = pathWithoutExtension.split("/");
        return fileNameParts[fileNameParts.length - 1];
    }


    private String replaceClassName(String template, String className) {
        return template.replace("$CLASSNAME", className);
    }

    private static String capitalizeFirstLetter(String className) {
        return className.substring(0, 1).toUpperCase() + className.substring(1);
    }
}
