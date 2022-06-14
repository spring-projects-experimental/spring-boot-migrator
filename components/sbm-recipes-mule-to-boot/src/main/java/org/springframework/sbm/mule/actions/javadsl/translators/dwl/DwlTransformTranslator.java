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

package org.springframework.sbm.mule.actions.javadsl.translators.dwl;

import org.mulesoft.schema.mule.ee.dw.TransformMessageType;
import org.springframework.sbm.java.util.Helper;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.Set;

@Component
public class DwlTransformTranslator implements MuleComponentToSpringIntegrationDslTranslator<TransformMessageType> {
    public static final String TRANSFORM_STATEMENT_CONTENT = ".transform($CLASSNAME::transform)";
    public static final String externalPackageName = "package com.example.javadsl;\n\n";

    /* Define the stubs for adding the transformation as a comment to be addressed */
    private static final String externalClassContentPrefixTemplate = externalPackageName +
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

    /* Define the TriggerMesh specific stubs when enabled. This will capture the transformation, and send it along
     * with the payload to the TriggerMesh Dataweave Transformation Service.
     */
    // Independent class to capture parts of the message header from the spring integration to inject into the transformation
    private static final String triggermeshDWPayload = "" +
            "package com.example.javadsl;\n\n" +
            "import lombok.Data;\n\n" +
            "@Data\n" +
            "public class TmDwPayload {\n" +
            "\tprivate String id;\n" +
            "\tprivate String source;\n" +
            "\tprivate String sourceType;\n" +
            "\tprivate String payload;\n" +
            "}\n";

    private static final String triggermeshPayloadHandlerContent = "" +
            ".<LinkedMultiValueMap<String, String>>handle((p, h) -> {\n" +
            "                    TmDwPayload dwPayload = new TmDwPayload();\n" +
            "                    dwPayload.setId(h.getId().toString());\n" +
            "                    dwPayload.setSourceType(h.get(\"contentType\").toString());\n" +
            "                    dwPayload.setSource(h.get(\"http_requestUrl\").toString());\n" +
            "                    dwPayload.setPayload(p.toString());\n" +
            "                    return dwPayload;\n" +
            "                })";
    private static final String triggermeshImportsTemplate = "import com.fasterxml.jackson.databind.ObjectMapper;\n\n" +
            "import java.net.URI;\n" +
            "import java.net.http.HttpClient;\n" +
            "import java.net.http.HttpRequest;\n" +
            "import java.net.http.HttpResponse;\n";

    private static final String triggermeshDWTransformationClass = "" +
            "\tpublic static class DataWeavePayload {\n" +
            "\t    public String input_data;\n" +
            "\t    public String spell;\n" +
            "\t    public String input_content_type;\n" +
            "\t    public String output_content_type;\n" +
            "\t};";

    private static final String triggermeshClassTemplate = externalPackageName + triggermeshImportsTemplate +
            "\npublic class $CLASSNAME {\n" +
            triggermeshDWTransformationClass + "\n\n" +
            "\tpublic static String transform(TmDwPayload payload) {\n" +
            "\t\tString uuid = payload.getId();\n" +
            "\t\tString url = System.getenv(\"K_SINK\");\n" + // NOTE: K_SINK is the URL for the target transformation service
            "\t\tHttpClient client = HttpClient.newHttpClient();\n" +
            "\t\tHttpRequest.Builder requestBuilder;\n" +
            "\t\tDataWeavePayload dwPayload = new DataWeavePayload();\n" +
            "\t\tif (payload.getSourceType().contains(\";\")) {\n" +
            "\t\t\tdwPayload.input_content_type = payload.getSourceType().split(\";\")[0];\n" +
            "\t\t} else {\n" +
            "\t\t\tdwPayload.input_content_type = payload.getSourceType();\n" +
            "\t\t}\n" +
            "\t\tdwPayload.output_content_type = \"$OUTPUT_CONTENT_TYPE\";\n" +
            "\t\t//TODO: Verify the spell conforms to Dataweave 2.x: https://docs.mulesoft.com/mule-runtime/4.4/migration-dataweave\n" +
            "\t\tdwPayload.spell = \"$DWSPELL\";\n" +
            "\t\tdwPayload.input_data = payload.getPayload();\n" +
            "\t\tString body;\n\n" +
            "\t\ttry {\n" +
            "\t\t\trequestBuilder = HttpRequest.newBuilder(new URI(url));\n" +
            "\t\t\tObjectMapper om = new ObjectMapper();\n" +
            "\t\t\tbody = om.writeValueAsString(dwPayload);\n" +
            "\t\t} catch (Exception e) {\n" +
            "\t\t\tSystem.out.println(\"Error sending request: \" + e.toString());\n" +
            "\t\t\treturn null;\n" +
            "\t\t}\n\n" +
            "\t\trequestBuilder.setHeader(\"content-type\", \"application/json\");\n" +
            "\t\trequestBuilder.setHeader(\"ce-specversion\", \"1.0\");\n" +
            "\t\trequestBuilder.setHeader(\"ce-source\", payload.getSource());\n" +
            "\t\trequestBuilder.setHeader(\"ce-type\", \"io.triggermesh.dataweave.transform\");\n" +
            "\t\trequestBuilder.setHeader(\"ce-id\", payload.getId());\n\n" +
            "\t\tHttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body)).build();\n\n" +
            "\t\ttry {\n" +
            "\t\t\tHttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());\n" +
            "\t\t\t // TODO: verify the response status and body\n" +
            "\t\t\treturn response.body();\n" +
            "\t\t} catch (Exception e) {\n" +
            "\t\t\tSystem.out.println(\"Error sending event: \" + e.toString());\n" +
            "\t\t\treturn null;\n" +
            "\t\t}\n" +
            "\t}\n";

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

        if (component.getSetPayload() != null) {
            if (isComponentReferencingAnExternalFile(component)) {
                return formExternalFileBasedDSLSnippet(component);
            }

            //return formEmbeddedDWLBasedDSLSnippet(component, Helper.sanitizeForBeanMethodName(flowName), id);
            return formTriggerMeshDWLBasedDSLSnippet(component, Helper.sanitizeForBeanMethodName(flowName), id);
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
        String tmTransformationContent = triggermeshClassTemplate
                .replace("$CLASSNAME", className)
                .replace("$OUTPUT_CONTENT_TYPE", outputContentType)
                .replace("$DWSPELL", sanitizeSpell(dwlSpell));

        // Build the dw payload
        return DslSnippet.builder()
                .externalClassContent(triggermeshDWPayload)
                .renderedSnippet(triggermeshPayloadHandlerContent + "\n" + replaceClassName(TRANSFORM_STATEMENT_CONTENT, className))
                .externalClassContent(tmTransformationContent)
                .requiredImports(Set.of("java.net.URI", "java.net.http.HttpClient", "java.net.http.HttpRequest", "java.net.http.HttpResponse", "com.fasterxml.jackson.databind.ObjectMapper"))
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
