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

import org.springframework.sbm.engine.recipe.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAspectJAutoProxy
@Aspect
@Slf4j
@ConditionalOnProperty(value = "logging.level.org.springframework.sbm.logging.StopWatchTraceInterceptor", havingValue = "TRACE")
public class StopWatchTraceInterceptor {

    int indentCount = 0;
    private final String INDENT = "    ";

    @Around(value = "execution(* org.springframework.sbm..*(..)) || execution(* org.openrewrite..*(..))")
    public Object aroundAspect(ProceedingJoinPoint pjp) throws Throwable {
        String signature = pjp.getSignature().toLongString();
        StopWatch stopWatch = new StopWatch(signature);
        stopWatch.start(signature);
        indentCount++;
        
        List<Object> argsList = Arrays.asList(pjp.getArgs());
        if(argsList.size() == 1) {
            if(argsList.get(0).getClass().isAssignableFrom(String.class)) {
                String arg = (String) argsList.get(0);
                String replace = "String";
                if(signature.contains("java.lang.String")) {
                    replace = "java.lang.String";
                }
                signature = signature.replace(replace, "\"" + arg + "\"");
            }
            if(argsList.get(0).getClass().isAssignableFrom(Recipe.class)) {
                Recipe recipe = (Recipe) argsList.get(0);
                String arg = "Recipe["+recipe.getName()+"]";
                String replace = "org.springframework.sbm.recipe.Recipe";
                signature = signature.replace(replace, "\"" + arg + "\"");
            }
        }

        String indention = INDENT.repeat(indentCount);
        log.trace(indention + "Start method: " + signature);

        Object returnValue = pjp.proceed();

        stopWatch.stop();
        log.trace(indention + pjp.getSignature() + " took: " + new BigDecimal(stopWatch.getTotalTimeSeconds()).setScale(4, RoundingMode.HALF_UP).toPlainString() + " sec.");
        indentCount--;
        return returnValue;
    }
}
