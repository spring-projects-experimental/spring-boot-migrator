package org.springframework.sbm.common.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AbsolutePathResourceFinder implements ProjectResourceFinder<Optional<ProjectResource>> {

    private final Path absoluteResourcePath;

    @Override
    public Optional<ProjectResource> apply(ProjectResourceSet projectResourceSet) {
        if (absoluteResourcePath == null || ! absoluteResourcePath.isAbsolute()) {
            throw new IllegalArgumentException("Given path '"+absoluteResourcePath+"' is not absolute");
        }
        Path searchForPath = absoluteResourcePath.normalize();
        return projectResourceSet
                .stream()
                .filter(r -> searchForPath.equals(r.getAbsolutePath()))
                .map(ProjectResource.class::cast)
                .findFirst();

    }
}
