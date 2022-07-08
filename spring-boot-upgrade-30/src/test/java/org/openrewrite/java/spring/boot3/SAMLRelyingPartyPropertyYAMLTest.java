package org.openrewrite.java.spring.boot3;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.yaml.YamlParser;
import org.openrewrite.yaml.tree.Yaml;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SAMLRelyingPartyPropertyYAMLTest {

    private final InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

    @Test
    void movePropertyTestSingle() {

        List<Result> result = runRecipe("""
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
                """.stripIndent());

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
        List<Result> result = runRecipe(
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
                        """.stripIndent()
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
        List<Result> result = runRecipe("""
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
                """.stripIndent());
        assertThat(result).hasSize(0);
    }

    @Test
    void resolveBasedOnCorrectHierarchy() {
        List<Result> result = runRecipe(
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
                        """.stripIndent()
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

    private List<Result> runRecipe(@Language("yml") String source) {
        List<Yaml.Documents> document = new YamlParser().parse(source);
        String recipeName = "org.openrewrite.java.spring.boot3.SAMLRelyingPartyPropertyYAMLMove";

        return RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(document, ctx);
    }
}
