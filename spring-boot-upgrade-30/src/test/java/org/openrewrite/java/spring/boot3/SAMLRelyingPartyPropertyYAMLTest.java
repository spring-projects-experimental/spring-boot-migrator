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
