package org.springframework.sbm.boot.upgrade_24_25.conditions;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.build.migration.conditions.AnyDependencyExistMatchingRegex;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

public class IsAnyMatchingSpringBootVersion implements Condition {

    /**
     * VersionPattern will be used for {@code startsWith} check against the version number found.
     */
    @NotNull
    @NotEmpty
    private List<String> versionPatterns;

    @Override
    public String getDescription() {
        return "Check if scanned application is Spring Boot application of given version";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return versionPatterns.stream()
                .anyMatch(p ->  new IsMatchingSpringBootVersion(p).evaluate(context));
    }

    public void setVersionPatterns(String versionPatterns) {
        this.versionPatterns = Arrays.asList(versionPatterns.split(","));
    }
}
