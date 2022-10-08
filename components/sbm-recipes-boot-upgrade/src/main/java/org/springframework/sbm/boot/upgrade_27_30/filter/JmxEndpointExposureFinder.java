package org.springframework.sbm.boot.upgrade_27_30.filter;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import org.springframework.sbm.properties.api.PropertiesSource;

import java.util.List;

public class JmxEndpointExposureFinder implements ProjectResourceFinder<List<? extends PropertiesSource>> {
    public static final String JMX_ENDPOINT_KEY = "management.endpoints.jmx.exposure.include";

    @Override
    public List<? extends PropertiesSource> apply(ProjectResourceSet projectResourceSet) {
        List<SpringBootApplicationProperties> springBootApplicationProperties = new SpringBootApplicationPropertiesResourceListFilter().apply(projectResourceSet);
        return springBootApplicationProperties.stream()
                .filter(find -> find.getProperty(JMX_ENDPOINT_KEY).isPresent())
                .toList();
    }
}
