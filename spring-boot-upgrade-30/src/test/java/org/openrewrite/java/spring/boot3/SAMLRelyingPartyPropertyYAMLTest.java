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
package org.openrewrite.java.spring.boot3;

import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SAMLRelyingPartyPropertyYAMLTest {

    @Test
    void movePropertyTestSingle() {
        List<Result> result = ConfigRecipeTestHelper.runRecipeOnYaml("""
                    spring:
                      security:
                        saml2:
                          relyingparty:
                            registration:
                              idpone:
                                identityprovider:
                                  entity-id: https://idpone.com
                                  sso-url: https://idpone.com
                                  verification:
                                    credentials:
                                      - certificate-location: "classpath:saml/idpone.crt"
                """.stripIndent(), "org.openrewrite.java.spring.boot3.SAMLRelyingPartyPropertyYAMLMove");

        assertThat(result).hasSize(1);

        assertThat(result.get(0).getAfter().printAll()).isEqualTo("""
                    spring:
                      security:
                        saml2:
                          relyingparty:
                            registration:
                              idpone:
                                assertingparty:
                                  entity-id: https://idpone.com
                                  sso-url: https://idpone.com
                                  verification:
                                    credentials:
                                      - certificate-location: "classpath:saml/idpone.crt"
                """.stripIndent());
    }

    @Test
    void movePropertyTestMultiple() {
        List<Result> result = ConfigRecipeTestHelper.runRecipeOnYaml(
                """
                            spring:
                              security:
                                saml2:
                                  relyingparty:
                                    registration:
                                      idpone:
                                        identityprovider:
                                          entity-id: https://idpone.com
                                          sso-url: https://idpone.com
                                          verification:
                                            credentials:
                                              - certificate-location: "classpath:saml/idpone.crt"
                                      okta:
                                        identityprovider:
                                          entity-id: https://idpone.com
                                          sso-url: https://idpone.com
                                          verification:
                                            credentials:
                                              - certificate-location: "classpath:saml/idpone.crt"
                        """.stripIndent(),
                "org.openrewrite.java.spring.boot3.SAMLRelyingPartyPropertyYAMLMove"
        );
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(
                """
                            spring:
                              security:
                                saml2:
                                  relyingparty:
                                    registration:
                                      idpone:
                                        assertingparty:
                                          entity-id: https://idpone.com
                                          sso-url: https://idpone.com
                                          verification:
                                            credentials:
                                              - certificate-location: "classpath:saml/idpone.crt"
                                      okta:
                                        assertingparty:
                                          entity-id: https://idpone.com
                                          sso-url: https://idpone.com
                                          verification:
                                            credentials:
                                              - certificate-location: "classpath:saml/idpone.crt"
                        """.stripIndent()
        );
    }

    @Test
    void movePropertyWhenCorrectHierarchyIsDetected() {
        List<Result> result = ConfigRecipeTestHelper.runRecipeOnYaml("""
                    some:
                      random:
                        thing:
                          relyingparty:
                            registration:
                              idpone:
                                identityprovider:
                                  entity-id: https://idpone.com
                                  sso-url: https://idpone.com
                                  verification:
                                    credentials:
                                      - certificate-location: "classpath:saml/idpone.crt"
                """.stripIndent(), "org.openrewrite.java.spring.boot3.SAMLRelyingPartyPropertyYAMLMove");
        assertThat(result).hasSize(0);
    }

    @Test
    void resolveBasedOnCorrectHierarchy() {
        List<Result> result = ConfigRecipeTestHelper.runRecipeOnYaml(
                """
                            spring:
                              security:
                                saml2:
                                  relyingparty:
                                    registration:
                                      idpone:
                                        identityprovider:
                                          entity-id: https://idpone.com
                                          sso-url: https://idpone.com
                                          verification:
                                            credentials:
                                              - certificate-location: "classpath:saml/idpone.crt"
                            relyingparty:
                                registration:
                                    something:
                                        identityprovider: 
                                            of: value
                        """.stripIndent(), "org.openrewrite.java.spring.boot3.SAMLRelyingPartyPropertyYAMLMove"
        );
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo(
                """
                    spring:
                      security:
                        saml2:
                          relyingparty:
                            registration:
                              idpone:
                                assertingparty:
                                  entity-id: https://idpone.com
                                  sso-url: https://idpone.com
                                  verification:
                                    credentials:
                                      - certificate-location: "classpath:saml/idpone.crt"
                    relyingparty:
                        registration:
                            something:
                                identityprovider: 
                                    of: value
                """.stripIndent());
    }

}
