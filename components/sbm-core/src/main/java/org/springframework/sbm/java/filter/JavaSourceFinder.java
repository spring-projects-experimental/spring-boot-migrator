package org.springframework.sbm.java.filter;

import org.springframework.sbm.build.api.JavaSourceSet;

/**
 * Filter java source set. Can be used to filter all java sources or a module specific
 * java sources
 * @param <T>
 */
public interface JavaSourceFinder<T> {
    public T apply(JavaSourceSet javaSourceSet);
}
