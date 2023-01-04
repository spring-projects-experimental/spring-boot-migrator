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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

class TagProvidersAndContributorsMigrationReportSectionTest {
    @Test
    @DisplayName("Tag providers and contributors migration should render")
    void withSingleModuleApplicationShouldRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .withSpringBootParentOf("2.7.5")
                         .withBuildFileHavingDependencies("io.micrometer:micrometer-core:1.10.2")
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Tag providers and contributors migration")
                         .fromProjectContext(context)
                         .shouldRenderAs("""
                                          === Tag providers and contributors migration
                                          
                                          ==== What Changed
                                          If your application is customizing metrics, you might see new deprecations in your codebase. In our new model, both tag providers and contributors are replaced by observation conventions. Let's take the example of the Spring MVC "http.server.requests" metrics instrumentation support in Spring Boot 2.x.
                                          
                                          If you are contributing additional `Tags` with `TagContributor` or only partially overriding a `TagProvider`, you should probably extend the `DefaultServerRequestObservationConvention` for your requirements:
                                          
                                          
                                          [source,java]
                                          ----
                                          public class ExtendedServerRequestObservationConvention extends DefaultServerRequestObservationConvention {
                                          
                                            @Override
                                            public KeyValues getLowCardinalityKeyValues(ServerRequestObservationContext context) {
                                              // here, we just want to have an additional KeyValue to the observation, keeping the default values
                                              return super.getLowCardinalityKeyValues(context).and(custom(context));
                                            }
                                          
                                            protected KeyValue custom(ServerRequestObservationContext context) {
                                              return KeyValue.of("custom.method", context.getCarrier().getMethod());
                                            }
                                          
                                          }
                                          ----
                                          
                                          
                                          If you are significantly changing metrics `Tags`, you are probably replacing the `WebMvcTagsProvider` with a custom implementation and contributing it as a bean. In this case, you should probably implement the convention for the observation you're interested in. Here, we'll implement `ServerRequestObservationConvention` - it's using `ServerRequestObservationContext` to extract information about the current request. You can then implement methods with your requirements in mind:
                                          
                                          
                                          [source,java]
                                          ----
                                          public class CustomServerRequestObservationConvention implements ServerRequestObservationContext {
                                          
                                            @Override
                                            public String getName() {
                                              // will be used for the metric name
                                              return "http.server.requests";
                                            }
                                          
                                            @Override
                                            public String getContextualName(ServerRequestObservationContext context) {
                                              // will be used for the trace name
                                              return "http " + context.getCarrier().getMethod().toLowerCase();
                                            }
                                          
                                            @Override
                                            public KeyValues getLowCardinalityKeyValues(ServerRequestObservationContext context) {
                                              return KeyValues.of(method(context), status(context), exception(context));
                                            }
                                          
                                            @Override
                                            public KeyValues getHighCardinalityKeyValues(ServerRequestObservationContext context) {
                                              return KeyValues.of(httpUrl(context));
                                            }
                                          
                                            protected KeyValue method(ServerRequestObservationContext context) {
                                              // You should reuse as much as possible the corresponding ObservationDocumentation for key names
                                              return KeyValue.of(ServerHttpObservationDocumentation.LowCardinalityKeyNames.METHOD, context.getCarrier().getMethod());
                                            }
                                          
                                            //...
                                          }
                                          ----
                                          
                                          
                                          In both cases, you can contribute those as beans to the application context and they will be picked up by the auto-configuration, effectively replacing the default ones.
                                          
                                          [source,java]
                                          ----
                                          @Configuration
                                          public class CustomMvcObservationConfiguration {
                                          
                                            @Bean
                                            public ExtendedServerRequestObservationConvention extendedServerRequestObservationConvention() {
                                              return new ExtendedServerRequestObservationConvention();
                                            }
                                          
                                          }
                                          ----
                                          
                                          
                                          You can also similar goals using a custom `ObservationFilter` - adding or removing key values for an observation.
                                          Filters do not replace the default convention and are used as a post-processing component.
                                          
                                          [source,java]
                                          ----
                                          public class ServerRequestObservationFilter implements ObservationFilter {
                                          
                                            @Override
                                            public Observation.Context map(Observation.Context context) {
                                              if (context instanceof ServerRequestObservationContext serverContext) {
                                                context.addLowCardinalityKeyValue(KeyValue.of("project", "spring"));
                                                String customAttribute = (String) serverContext.getCarrier().getAttribute("customAttribute");
                                                context.addLowCardinalityKeyValue(KeyValue.of("custom.attribute", customAttribute));
                                              }
                                              return context;
                                            }
                                          }
                                          ----
                                          
                                          You can contribute `ObservationFilter` beans to your application and Spring Boot will auto-configure them with the `ObservationRegistry`.
                                          
                                          ==== Why is the application affected
                                          Actually, we don't know if the scanned application is really affected by this change.
                                          But we found a dependency matching regex `io\\.micrometer\\:micrometer-.*`.
                                          This indicates that the scanned application might be affected.
                                          
                                          ==== Remediation
                                          [IMPORTANT]
                                          ====
                                          This section has been automatically generated from the https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#tag-providers-and-contributors-migration[Spring Boot 3.0 Migration Guide^, role="ext-link"]. +
                                          **Please consider contributing to issue https://github.com/spring-projects-experimental/spring-boot-migrator/issues/664[#664^, role="ext-link"]**
                                          ====
                                          

                                          """);
    }

    @Test
    @DisplayName("Tag providers and contributors migration should not render")
    void shouldNotRender() {
         ProjectContext context = TestProjectContext
                         .buildProjectContext()
                         .build();

                 SpringBootUpgradeReportTestSupport.generatedSection("Tag providers and contributors migration")
                         .fromProjectContext(context)
                         .shouldNotRender();
    }
}
