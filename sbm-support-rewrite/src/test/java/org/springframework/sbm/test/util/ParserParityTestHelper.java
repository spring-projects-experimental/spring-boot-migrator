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
package org.springframework.sbm.test.util;

import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;
import org.openrewrite.maven.MavenSettings;
import org.openrewrite.style.Style;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.sbm.boot.autoconfigure.SbmSupportRewriteConfiguration;
import org.springframework.sbm.parsers.ParserProperties;
import org.springframework.sbm.parsers.RewriteProjectParser;
import org.springframework.sbm.parsers.RewriteProjectParsingResult;
import org.springframework.sbm.parsers.maven.ComparingParserFactory;
import org.springframework.sbm.parsers.maven.RewriteMavenProjectParser;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class ParserParityTestHelper {
    private final Path baseDir;
    private ParserProperties parserProperties = new ParserProperties();
    private boolean isParallelParse = true;
    private ExecutionContext executionContext;

    public ParserParityTestHelper(Path baseDir) {
        this.baseDir = baseDir;
    }

    public static ParserParityTestHelper scanProjectDir(Path baseDir) {
        ParserParityTestHelper helper = new ParserParityTestHelper(baseDir);
        return helper;
    }

    /**
     * Sequentially parse given project using tested parser and then comparing parser.
     * The parsers are executed in parallel by default.
     */
    public ParserParityTestHelper parseSequentially() {
        this.isParallelParse = false;
        return this;
    }

    public ParserParityTestHelper withParserProperties(ParserProperties parserProperties) {
        this.parserProperties = parserProperties;
        return this;
    }

    public void verifyParity() {
        verifyParity((comparingParsingResult, testedParsingResult) -> {
            // nothing extra to verify
        });
    }

    public void verifyParity(ParserResultParityChecker parserResultParityChecker) {
        try {
            RewriteProjectParsingResult comparingParserResult = null;
            RewriteProjectParsingResult testedParserResult = null;

            if(isParallelParse) {
                CountDownLatch latch = new CountDownLatch(2);

                ExecutorService threadPool
                        = Executors.newFixedThreadPool(2);

                AtomicReference<RewriteProjectParsingResult> testedParsingResultRef = new AtomicReference<>();
                AtomicReference<RewriteProjectParsingResult> comparingParsingResultRef = new AtomicReference<>();

                threadPool.submit(() -> {
                    System.out.println("Start parsing with RewriteProjectParser");
                    RewriteProjectParsingResult tmpTestedParserResult = parseWithRewriteProjectParser(parserProperties, executionContext);
                    testedParsingResultRef.set(tmpTestedParserResult);
                    latch.countDown();
                });

                threadPool.submit(() -> {
                    System.out.println("Start parsing with RewriteMavenProjectParser");
                    RewriteProjectParsingResult tmpComparingParserResult = parseWithComparingParser(parserProperties, executionContext);
                    comparingParsingResultRef.set(tmpComparingParserResult);
                    latch.countDown();
                });

                latch.await();

                comparingParserResult = comparingParsingResultRef.get();
                testedParserResult = testedParsingResultRef.get();
            } else {
                testedParserResult = parseWithRewriteProjectParser(parserProperties, executionContext);
                comparingParserResult = parseWithComparingParser(parserProperties, executionContext);
            }



            // Number of parsed sources should always be the same
            assertThat(comparingParserResult.sourceFiles().size())
                    .as(renderErrorMessage(comparingParserResult, testedParserResult))
                    .isEqualTo(testedParserResult.sourceFiles().size());

            // The paths of sources should be the same
            List<String> comparingResultPaths = comparingParserResult.sourceFiles().stream().map(sf -> baseDir.resolve(sf.getSourcePath()).toAbsolutePath().normalize().toString()).toList();
            List<String> testedResultPaths = testedParserResult.sourceFiles().stream().map(sf -> baseDir.resolve(sf.getSourcePath()).toAbsolutePath().normalize().toString()).toList();
            assertThat(testedResultPaths).containsExactlyInAnyOrder(comparingResultPaths.toArray(String[]::new));

            // The Markers of all resources should be the same
            verifyMarkersAreTheSame(comparingParserResult, testedParserResult);

            parserResultParityChecker.accept(testedParserResult, comparingParserResult);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String renderErrorMessage(RewriteProjectParsingResult comparingParserResult, RewriteProjectParsingResult testedParserResult) {
        List<SourceFile> collect = new ArrayList<>();
        if(comparingParserResult.sourceFiles().size() > testedParserResult.sourceFiles().size()) {
            collect = comparingParserResult.sourceFiles().stream()
                    .filter(element -> !testedParserResult.sourceFiles().contains(element))
                    .collect(Collectors.toList());
        } else {
            collect = testedParserResult.sourceFiles().stream()
                    .filter(element -> !comparingParserResult.sourceFiles().contains(element))
                    .collect(Collectors.toList());
        }

        return "ComparingParserResult had %d sourceFiles whereas TestedParserResult had %d sourceFiles. Files were %s".formatted(comparingParserResult.sourceFiles().size(), testedParserResult.sourceFiles().size(), collect);
    }

    private void verifyMarkersAreTheSame(RewriteProjectParsingResult comparingParserResult, RewriteProjectParsingResult testedParserResult) {
        List<SourceFile> comparingSourceFiles = comparingParserResult.sourceFiles();
        List<SourceFile> testedSourceFiles = testedParserResult.sourceFiles();

        // bring to same order
        comparingSourceFiles.sort(Comparator.comparing(SourceFile::getSourcePath));
        testedSourceFiles.sort(Comparator.comparing(SourceFile::getSourcePath));

        for(SourceFile curComparingSourceFile : comparingSourceFiles) {
            int index = comparingSourceFiles.indexOf(curComparingSourceFile);
            SourceFile curTestedSourceFile = testedSourceFiles.get(index);

            Markers comparingMarkers = curComparingSourceFile.getMarkers();
            List<Marker> comparingMarkersList = comparingMarkers.getMarkers();
            Markers testedMarkers = curTestedSourceFile.getMarkers();
            List<Marker> testedMarkersList = testedMarkers.getMarkers();

            assertThat(comparingMarkersList)
                    .usingRecursiveComparison()
                    .withStrictTypeChecking()
                    .ignoringCollectionOrder()
                    .ignoringFields(
                            // classpath compared further down
                            "classpath",
                            // FIXME: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/982
                            "styles"
                    )
                    .ignoringFieldsOfTypes(
                            UUID.class,
                            // FIXME: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/880
                            MavenSettings.class,
                            // FIXME: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/982
                            Style.class)
                    .isEqualTo(testedMarkersList);

            if(curComparingSourceFile.getMarkers().findFirst(JavaSourceSet.class).isPresent()) {
                // Tested parser must have JavaSourceSet marker when comparing parser has it
                assertThat(testedMarkers.findFirst(JavaSourceSet.class)).isPresent();

                // assert classpath equality
                List<String> comparingClasspath = comparingMarkers.findFirst(JavaSourceSet.class).get().getClasspath().stream().map(JavaType.FullyQualified::getFullyQualifiedName).toList();
                List<String> testedClasspath = testedMarkers.findFirst(JavaSourceSet.class).get().getClasspath().stream().map(JavaType.FullyQualified::getFullyQualifiedName).toList();

                assertThat(comparingClasspath.size()).isEqualTo(testedClasspath.size());

                assertThat(comparingClasspath)
                        .withFailMessage(() -> {
                            List<String> additionalElementsInComparingClasspath = comparingClasspath.stream()
                                    .filter(element -> !testedClasspath.contains(element))
                                    .collect(Collectors.toList());

                            if(!additionalElementsInComparingClasspath.isEmpty()) {
                                return "Classpath of comparing and tested parser differ: comparing classpath contains additional entries: %s".formatted(additionalElementsInComparingClasspath);
                            }

                            List<String> additionalElementsInTestedClasspath = testedClasspath.stream()
                                    .filter(element -> !comparingClasspath.contains(element))
                                    .collect(Collectors.toList());

                            if(!additionalElementsInTestedClasspath.isEmpty()) {
                                return "Classpath of comparing and tested parser differ: tested classpath contains additional entries: %s".formatted(additionalElementsInTestedClasspath);
                            }

                            return "Bang!";
                        })
                        .containsExactlyInAnyOrder(testedClasspath.toArray(String[]::new));
            }

        }
    }

    private RewriteProjectParsingResult parseWithComparingParser(ParserProperties parserProperties, ExecutionContext executionContext) {
        RewriteMavenProjectParser comparingParser = new ComparingParserFactory().createComparingParser(parserProperties);
        if(executionContext != null) {
            return comparingParser.parse(baseDir, executionContext);
        } else {
            return comparingParser.parse(baseDir);
        }
    }

    private RewriteProjectParsingResult parseWithRewriteProjectParser(ParserProperties parserProperties, ExecutionContext executionContext) {
        AtomicReference<RewriteProjectParsingResult> atomicRef = new AtomicReference<>();
        new ApplicationContextRunner().withUserConfiguration(SbmSupportRewriteConfiguration.class)
                .withBean("parser-org.springframework.sbm.parsers.ParserProperties", ParserProperties.class, () -> parserProperties)
                .run(appCtx -> {
                    RewriteProjectParser sut = appCtx.getBean(RewriteProjectParser.class);
                    RewriteProjectParsingResult testedParserResult = sut.parse(baseDir);
                    atomicRef.set(testedParserResult);
//                    RewriteProjectParsingResult parsingResult2 = sut.parse(tempDir);
//                    verifyParsingResult(parsingResult2, RewriteProjectParserParityTest.ParserType.SBM);
//
//                    InMemoryExecutionContext executionContext1 = createExecutionContext();
//                    RewriteMavenProjectParser mavenProjectParser = new ComparingParserFactory().createComparingParser(comparingParserProperties);
//                    RewriteProjectParsingResult parsingResult = mavenProjectParser.parse(tempDir, executionContext1);
//                    verifyParsingResult(parsingResult, RewriteProjectParserParityTest.ParserType.COMPARING);
                });
        return atomicRef.get();
    }

    public ParserParityTestHelper withExecutionContextForComparingParser(ExecutionContext executionContext) {
        this.executionContext = executionContext;
        return this;
    }

    public interface ParserResultParityChecker extends BiConsumer<RewriteProjectParsingResult, RewriteProjectParsingResult> {
        @Override
        void accept(RewriteProjectParsingResult comparingParsingResult, RewriteProjectParsingResult testedParsingResult);
    }
}
