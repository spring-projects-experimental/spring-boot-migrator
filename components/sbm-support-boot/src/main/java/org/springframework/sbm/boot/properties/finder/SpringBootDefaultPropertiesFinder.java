package org.springframework.sbm.boot.properties.finder;

import org.openrewrite.properties.tree.Properties;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.util.Optional;

public class SpringBootDefaultPropertiesFinder implements ProjectResourceFinder<Optional<SpringBootApplicationProperties>> {

    @Override
    public Optional<SpringBootApplicationProperties> apply(ProjectResourceSet projectResourceSet) {
        return projectResourceSet.stream()
                .filter(r -> r.getSourceFile() instanceof Properties.File)
                .map(r -> new SpringBootApplicationProperties(r.getAbsoluteProjectDir(), (Properties.File) r.getSourceFile()))
                .filter(SpringBootApplicationProperties::isDefaultProperties)
                .findFirst();
    }
}
