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
class ScanValueProvider implements ValueProvider { // extend to only complete for exact type matches

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
