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
package org.springframework.sbm.shell;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

/**
 * Auto complete argument to scan method for easier navigation.
 * Based on org.springframework.shell.standard.FileValueProvider.
 *
 * @author Tim te Beek
 */
@Component
class ScanValueProvider implements ValueProvider {

    @Override
    public List<CompletionProposal> complete(CompletionContext completionContext) {
        String input = completionContext.currentWordUpToCursor();
        int lastSlash = input.lastIndexOf(File.separatorChar);
        Path dir = lastSlash > -1 ? Paths.get(input.substring(0, lastSlash + 1)) : Paths.get("");
        String prefix = input.substring(lastSlash + 1, input.length());

        BiPredicate<Path, BasicFileAttributes> matchingPrefix = (p, a) -> p.getFileName() != null
                && p.toFile().isDirectory()
                && p.getFileName().toString().startsWith(prefix);
        try (Stream<Path> stream = Files.find(dir, 1, matchingPrefix, FOLLOW_LINKS)) {
            return stream
                    .map(Path::toString)
                    .map(CompletionProposal::new)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
