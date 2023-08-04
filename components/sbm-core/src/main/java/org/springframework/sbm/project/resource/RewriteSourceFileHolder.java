/*
 * Copyright 2021 - 2022 the original author or authors.
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

import lombok.Getter;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.lang.Nullable;

import java.nio.file.Path;
import java.util.UUID;

public class RewriteSourceFileHolder<T extends SourceFile> extends BaseProjectResource implements InternalProjectResource {

    private T sourceFile;
    @Getter
    final private Path absoluteProjectDir;
    private SourceFile sourceFile2;

    /**
     * @param absoluteProjectDir the absolute path to project root
     * @param sourceFile         the OpenRewrite {@code SourceFile}
     */
    public RewriteSourceFileHolder(Path absoluteProjectDir, T sourceFile) {
        this.absoluteProjectDir = absoluteProjectDir;
        this.sourceFile = sourceFile;
        if (!this.absoluteProjectDir.isAbsolute()) {
            throw new IllegalArgumentException(String.format("Given projectDir '%s' is not absolute.", absoluteProjectDir));
        }
    }

    public String print() {
        try {
            return sourceFile.printAll();
        } catch (Exception e) {
            throw new RuntimeException("Exception while printing '%s'".formatted(sourceFile.getSourcePath()), e);
        }
    }

    @Override
    public Path getSourcePath() {
        return sourceFile.getSourcePath();
    }

    @Override
    public Path getAbsolutePath() {
        return absoluteProjectDir.resolve(getSourcePath()).normalize().toAbsolutePath();
    }

    /**
     * Move the represented resource to another location.
     * <p>
     * The given {@code newPath} will be relativized if absolute.
     * The returned instance represents the same file at the new location.
     *
     * @param newPath relative path with filename for the destination
     */
    @Override
    public void moveTo(Path newPath) {
        if (newPath.isAbsolute()) {
            newPath = absoluteProjectDir.relativize(newPath);
        }
        if (absoluteProjectDir.resolve(newPath).toFile().isDirectory()) {
            newPath = newPath.resolve(this.getAbsolutePath().getFileName());
        }
        sourceFile = (T) sourceFile.withSourcePath(newPath);
        this.markChanged();
    }

    public T getSourceFile() {
        return sourceFile;
    }

    /**
     * Replace current source file with {@code fixedSourceFile}.
     * <p>
     * If {@code fixedSourceFile.print()} differs from current file content,
     * source file is marked as changed.
     *
     * @param fixedSourceFile the new source file
     */
    public void replaceWith(@Nullable SourceFile fixedSourceFile) {
        if (sourceFile != null && !sourceFile.printAll().equals(fixedSourceFile.printAll())) {
            markChanged();
        }
        sourceFile = (T) fixedSourceFile;
    }

    public void markChanged() {
        this.isChanged = true;
    }

    public UUID getId() {
        return this.getSourceFile().getId();
    }

    public Class<? extends SourceFile> getType() {
        return getSourceFile().getClass();
    }

    @Override
    public String toString() {
        return getAbsolutePath().toString();
    }
}
