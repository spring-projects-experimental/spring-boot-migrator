package org.springframework.sbm.build.api;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SpringManagedDependenciesTest {

    @Test
    public void pullBootStarter274Dependencies_expectJakartaAnnotationDependency(){
        String jakartaCoordinates = "jakarta.annotation:jakarta.annotation-api:1.3.5";

        assertThat( SpringManagedDependencies.by("org.springframework.boot", "spring-boot-starter", "2.7.4")
                                     .stream()
                                     .map(Dependency::getCoordinates)
                                     .anyMatch(jakartaCoordinates::equals)
        ).isTrue();
    }
}
