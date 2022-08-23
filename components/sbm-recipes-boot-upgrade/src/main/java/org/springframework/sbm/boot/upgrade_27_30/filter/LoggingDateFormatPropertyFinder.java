package org.springframework.sbm.boot.upgrade_27_30.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import org.springframework.sbm.properties.api.PropertiesSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class LoggingDateFormatPropertyFinder implements ProjectResourceFinder<List<? extends PropertiesSource>> {

    private static final String LOGGING_DATE_FORMAT_KEY = "logging.pattern.dateformat";

    @Override
    public List<? extends PropertiesSource> apply(ProjectResourceSet projectResourceSet) {
        List<SpringBootApplicationProperties> springBootApplicationProperties = new SpringBootApplicationPropertiesResourceListFilter().apply(projectResourceSet);

        return springBootApplicationProperties.stream()
                .filter(x -> x.getProperty(LOGGING_DATE_FORMAT_KEY).isPresent())
                .toList();
    }
}
