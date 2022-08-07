package org.springframework.sbm.java.filter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.sbm.build.api.JavaSourceSet;
import org.springframework.sbm.java.api.JavaSource;

import java.util.List;

@RequiredArgsConstructor
public class AnnotatedJavaClassFinder implements JavaSourceFinder<List<? extends JavaSource>> {

    @NonNull
    private final String annotation;

    @Override
    public List<? extends JavaSource> apply(JavaSourceSet projectResourceSet) {

        return projectResourceSet.stream()
                .filter(x -> JavaSource.class.isAssignableFrom(x.getClass()))
                .map(JavaSource.class::cast)
                .filter(x -> x.hasAnnotation(annotation))
                .toList();
    }
}
