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
package org.springframework.sbm.jee.jaxws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Alex Boyko
 */
@Getter
@Setter
public class WebServiceOperation {

    private String name;

    private String serviceMethodName;

    private Type input;

    private Type output;

    @Getter
    @Setter
    public static class Type {
        private String packageName;
        private String simpleName;
        private String[] fields;

        public String getFqName() {
            return packageName + "." + simpleName;
        }
    }

    public String createSnippet(String serviceFieldName, String nsUriConstant) {
        StringBuilder sb = new StringBuilder();
        sb.append("@PayloadRoot(namespace = " + nsUriConstant + ", localPart = \"" + StringUtils.uncapitalize(input.getSimpleName()) + "\")\n");
        String params = Arrays.stream(input.fields).map(f -> "request.get" + StringUtils.capitalize(f) + "()").collect(Collectors.joining(", "));
        String serviceCall = serviceFieldName + "." + serviceMethodName + "(" + params + ")";
        if (output == null) {
            sb.append("public void " + name + "(@RequestPayload " + input.getSimpleName() + " request) {\n");
            sb.append("\t" + serviceCall + ";\n");
            sb.append("}\n");
        } else {
            sb.append("@ResponsePayload\n");
            sb.append("public " + output.simpleName + " " + name + "(@RequestPayload " + input.getSimpleName() + " request) {\n");
            sb.append("\t" + output.simpleName + " response = new " + output.getSimpleName() + "();\n");
            sb.append("\tresponse.set" + StringUtils.capitalize(output.fields[0]) + "(" + serviceCall + ");\n");
            sb.append("\treturn response;\n");
            sb.append("}\n");
        }
        return sb.toString();
    }

    public String[] getImports() {
        if (output == null) {
            return new String[]{
                    "org.springframework.ws.server.endpoint.annotation.PayloadRoot",
                    "org.springframework.ws.server.endpoint.annotation.RequestPayload",
                    input.getFqName(),
            };
        } else {
            return new String[]{
                    "org.springframework.ws.server.endpoint.annotation.PayloadRoot",
                    "org.springframework.ws.server.endpoint.annotation.RequestPayload",
                    "org.springframework.ws.server.endpoint.annotation.ResponsePayload",
                    input.getFqName(),
                    output.getFqName()
            };
        }
    }

}
