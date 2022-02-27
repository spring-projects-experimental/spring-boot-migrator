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
package org.springframework.sbm.test;

import org.springframework.boot.context.annotation.Configurations;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ContextConsumer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

public class SpringBeanProvider {

    @Configuration
    @ComponentScan("org.springframework.sbm")
    public static class ComponentScanConfiguration { }

    public static void run(ContextConsumer<AssertableApplicationContext> testcode, Class<?>... springBeans) {
        ApplicationContextRunner contextRunner = new ApplicationContextRunner();
        for (Class<?> springBean : springBeans) {
            if(springBean.isAssignableFrom(Configurations.class)) {
                Configurations c = Configurations.class.cast(springBean);
                contextRunner.withConfiguration(c);
            } else {
                contextRunner = contextRunner.withBean(springBean);
            }
        }
        contextRunner.run(testcode);
    }
}