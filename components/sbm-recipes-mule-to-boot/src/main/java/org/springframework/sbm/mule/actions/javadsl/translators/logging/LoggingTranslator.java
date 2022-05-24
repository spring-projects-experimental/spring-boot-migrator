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
package org.springframework.sbm.mule.actions.javadsl.translators.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mulesoft.schema.mule.core.LoggerType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;
import org.springframework.sbm.mule.actions.javadsl.translators.MuleComponentToSpringIntegrationDslTranslator;
import org.springframework.sbm.mule.actions.javadsl.translators.common.ExpressionLanguageTranslator;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurations;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.*;

/**
 * Translates {@code <logger/>} to SI DSL.
 *
 * https://docs.mulesoft.com/mule-runtime/3.9/logging
 * https://docs.mulesoft.com/mule-runtime/3.9/logger-component-reference
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingTranslator implements MuleComponentToSpringIntegrationDslTranslator<LoggerType> {

    private Set<String> requiredImports = new HashSet<>();
    private final ExpressionLanguageTranslator expressionLanguageTranslator;

    @Override
    public Class getSupportedMuleType() {
        return LoggerType.class;
    }

    public DslSnippet translate(int id, LoggerType loggerType, QName name, MuleConfigurations muleConfigurations, String flowName, Map<Class, MuleComponentToSpringIntegrationDslTranslator> translatorsMap) {

        StringBuffer sb = new StringBuffer();

        sb.append(".log(");

/* Spring Integration DSL log() API
        log()

        log(Level level)
        log(String category)
        log(Expression logExpression)

        log(Level level, String category)
        log(Level level, Expression logExpression)
        log(String category, Expression logExpression)

        log(Level level, String category, String logExpression)


        log(Function<Message<P>, Object> function)


        log(Level level, Function<Message<P>, Object> function)
        log(String category, Function<Message<P>, Object> function)
        log(Level level, String category, Function<Message<P>, Object> function)
*/
        Map<String, String> parameter = new HashMap<>();

        // log level
        String logLevel = loggerType.getLevel();
        if(logLevel != null) {
            String siLogLevel = translateLogLevel(logLevel);
            parameter.put("level", siLogLevel);
            requiredImports.add("org.springframework.integration.handler.LoggingHandler");
        }

        // category
        String category = loggerType.getCategory();
        if(category != null) {
            parameter.put("category", category);
        }

        // message
        String message = loggerType.getMessage();
        if(message != null) {
            message = expressionLanguageTranslator.translate(message);
            parameter.put("message", message);
        }

        if( ! parameter.isEmpty()) {
            if(parameter.size() == 1 && parameter.containsKey("level")) {
                sb.append(parameter.get("level"));
            } else if(parameter.size() == 1 && parameter.containsKey("category")) {
                sb.append("\"").append(parameter.get("category")).append("\"");
            } else if(parameter.size() == 1 && parameter.containsKey("message")) {
                sb.append("\"").append(parameter.get("message")).append("\"");
            }

            else if(parameter.size() == 2 && parameter.containsKey("level") && parameter.containsKey("message")) {
                sb.append(parameter.get("level")).append(", ").append("\"").append(parameter.get("message")).append("\"");
            } else if(parameter.size() == 2 && parameter.containsKey("level") && parameter.containsKey("category")) {
                sb.append(parameter.get("level")).append(", ").append("\"").append(parameter.get("category")).append("\"");
            } else if(parameter.size() == 2 && parameter.containsKey("category") && parameter.containsKey("message")) {
                sb.append("\"").append(parameter.get("category")).append("\", \"").append(parameter.get("message")).append("\"");
            }

            else if(parameter.size() == 3){
                sb.append(parameter.get("level")).append(", \"").append(parameter.get("category")).append("\", \"").append(parameter.get("message")).append("\"");
            }
        }


        sb.append(")");

        DslSnippet dslSnippet = new DslSnippet(sb.toString(), requiredImports, Collections.emptySet(), Collections.emptySet());
        return dslSnippet;
    }

    private String translateLogLevel(String logLevel) {
        Set<String> acceptedLoggerLevel = Set.of("DEBUG", "ERROR", "INFO", "TRACE", "WARN");
        if(logLevel == null || logLevel.isEmpty() || ! acceptedLoggerLevel.contains(logLevel)) {
            log.warn(String.format("Found unknown loglevel '%s'. Will set 'INFO' instead", logLevel));
            // FIXME: verify that info is default level
            return "LoggingHandler.Level.INFO";
        } else {
            return "LoggingHandler.Level." + logLevel;
        }
    }
}
