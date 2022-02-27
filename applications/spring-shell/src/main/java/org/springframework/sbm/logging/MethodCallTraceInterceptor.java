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
package org.springframework.sbm.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableAspectJAutoProxy
@Aspect
@Slf4j
@ConditionalOnProperty(value = "logging.level.org.springframework.sbm.logging.MethodCallTraceInterceptor", havingValue = "TRACE")
public class MethodCallTraceInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    int indentCount = 0;
    private final String INDENT = "    ";

    @Around(value = "execution(* org.springframework.sbm..*(..)) || execution(* org.openrewrite..*(..))")
    public Object aroundAspect(ProceedingJoinPoint pjp) throws Throwable {
        String signature = pjp.getSignature().toLongString();
        List<Object> argsList = Arrays.asList(pjp.getArgs());
        String args = renderArgs(argsList);

        log.trace("Call to " + signature);
        log.trace("Params: " + args);

        Object returned = pjp.proceed();

        log.trace(signature +" returned");
        if( ! signature.contains(" void ")) {
            log.trace("Return value was " + renderReturnValue(returned));
        }


        return returned;
    }

    private String renderReturnValue(Object returned) {
        try {
            if(returned == null) return "null";
            return "[" + returned.getClass() +"] => \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(returned);
        } catch (JsonProcessingException e) {
            return "Could not serialize '"+returned.getClass()+"'";
        }
    }

    private String renderArgs(List<Object> argsList) {
        return argsList.stream()
                .map(a -> {
                    return renderReturnValue(a);

                }).collect(Collectors.joining("\n"));
    }
}
