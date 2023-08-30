/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.project.resource;

import org.openrewrite.SourceFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: make package private
public class ProjectResourceSet {

    private final List<RewriteSourceFileHolder<? extends SourceFile>> projectResources = new ArrayList<>();

    public ProjectResourceSet(List<RewriteSourceFileHolder<? extends SourceFile>> projectResources) {
        this.projectResources.addAll(projectResources);
    }

    public ProjectResourceSet() {
    }

    public List<RewriteSourceFileHolder<? extends SourceFile>> list() {
        return stream().toList();
    }

    public Stream<RewriteSourceFileHolder<? extends SourceFile>> stream() {
        return projectResources.stream().filter(r -> r != null && !r.isDeleted());
    }

    public ProjectResource get(int index) {
        return list().get(index);
    }

    public void add(RewriteSourceFileHolder<? extends SourceFile> newResource) {
        projectResources.add(newResource);
    }

    public void replace(int index, RewriteSourceFileHolder<? extends SourceFile> newResource) {
        projectResources.set(index, newResource);
    }

    public void replace(Path path, RewriteSourceFileHolder<? extends SourceFile> newResource) {
        int index = indexOf(path);
        projectResources.set(index, newResource);
    }

    public int size() {
        return projectResources.size();
    }

//    /**
//     * @deprecated use {@link ProjectContext#getFilteredResources(ProjectResourcesFilter)}
//     * with {@link org.springframework.sbm.project.resource.filter.GenericTypeFilter}
//     */
//    @Deprecated(forRemoval = true)
//    public <T> List<RewriteSourceFileHolder<T>> getProjections(Class<T> projectionClass) {
//        return typeFilteredList(projectionClass);
//    }

    public int indexOf(Path absolutePath) {
        return projectResources.stream()
                .map(ProjectResource::getAbsolutePath)
                .collect(Collectors.toList())
                .indexOf(absolutePath);
    }

    void clearDeletedResources() {
        Iterator<RewriteSourceFileHolder<? extends SourceFile>> iterator = this.projectResources.iterator();
        while(iterator.hasNext()) {
            RewriteSourceFileHolder<? extends SourceFile> current = iterator.next();
            if(current.isDeleted()) {
                iterator.remove();
            }
        }
    }

    public Stream<RewriteSourceFileHolder<? extends SourceFile>> streamIncludingDeleted() {
        return projectResources.stream();
    }
//
//    public Stream<ProjectResource> filteredStream(ProjectResourceFilter filter) {
//        return projectResources.stream()
//                .filter(filter::satisfies);
//    }

//    public <T extends ProjectResource> Stream<T> classFilteredStream(Class<T> clazz) {
//        return filteredStream(pr -> clazz.isAssignableFrom(pr.getClass()))
//                .map(clazz::cast);
//    }
//
//    public <T> Stream<RewriteSourceFileHolder<T>> typeFilteredStream(Class<T> type) {
//        return filteredStream(pr -> Objects.nonNull(pr.getType()) && type.isAssignableFrom(pr.getType()))
//                .map(pr -> (RewriteSourceFileHolder<T>)pr);
//    }
//
//    public List<ProjectResource> filteredList(ProjectResourceFilter filter) {
//        return filteredStream(filter)
//                .collect(Collectors.toList());
//    }
//
//    public <T extends ProjectResource> List<T> classFilteredList(Class<T> clazz) {
//        return classFilteredStream(clazz)
//                .collect(Collectors.toList());
//    }
//
//    public <T> List<RewriteSourceFileHolder<T>> typeFilteredList(Class<T> type) {
//        return typeFilteredStream(type)
//                .collect(Collectors.toList());
//    }
}
