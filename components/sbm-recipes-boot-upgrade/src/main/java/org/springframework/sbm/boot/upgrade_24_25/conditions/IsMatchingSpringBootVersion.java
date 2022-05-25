package org.springframework.sbm.boot.upgrade_24_25.conditions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.build.migration.conditions.AnyDependencyExistMatchingRegex;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class IsMatchingSpringBootVersion implements Condition {

    /**
     * VersionPattern will be used for {@code startsWith} check against the version number found.
     */
    @NotNull
    @Setter
    private String versionPattern;

    @Override
    public String getDescription() {
        return "Check if scanned application is Spring Boot application of given version";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        HasSpringBootParentOfVersion hasSpringBootParentOfVersion = new HasSpringBootParentOfVersion();
        hasSpringBootParentOfVersion.setVersionPattern(versionPattern);
        String versionRegex = versionPattern.replace(".", "\\.") + ".*";
        AnyDependencyExistMatchingRegex anyDependencyExistMatchingRegex = new AnyDependencyExistMatchingRegex(List.of("org\\.springframework\\.boot\\:.*\\:" + versionRegex));
        return hasSpringBootParentOfVersion.evaluate(context) || anyDependencyExistMatchingRegex.evaluate(context);
    }
}
