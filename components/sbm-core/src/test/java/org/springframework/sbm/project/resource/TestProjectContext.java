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

import org.jetbrains.annotations.NotNull;
import org.openrewrite.Parser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.build.migration.MavenPomCacheProvider;
import org.springframework.sbm.build.resource.BuildFileResourceWrapper;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextFactory;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.java.JavaSourceProjectResourceWrapper;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactory;
import org.springframework.sbm.java.refactoring.JavaRefactoringFactoryImpl;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.java.util.JavaSourceUtil;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.TestDummyResource;
import org.springframework.sbm.project.parser.DependencyHelper;
import org.springframework.sbm.project.parser.ProjectContextInitializer;
import org.springframework.sbm.project.parser.RewriteMavenParserFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Creates {@code ProjectContext} initialized for different testing scenarios.
 *
 * === Simple Project Context
 *
 * [source, java]
 * .....
 * // ProjectContext with empty default pom.xml and ApplicationProperties with default settings.
 * TestProjectContext.buildProjectContext().build();
 *
 * // create default pom.xml with given content
 * TestProjectContext.buildProjectContext()
 *                      .withMaven()
 *                      .build();
 *
 * // create pom.xml at given location with given content
 * TestProjectContext.buildProjectContext()
 *                      .addProjectResource("module1/pom.xml", "...pomSource...")
 *                      .build();
 *
 * // create application.properties at given location with given content
 * TestProjectContext.buildProjectContext()
 *                      .addProjectResource("module1/src/main/resources/application.properties", "prop=value")
 *                      .build();
 *
 * // use mock for Maven default build file
 * TestProjectContext.buildProjectContext()
 *                      .withMockedMaven(buildFileMock)
 *                      .build();
 *
 * // adds a java files, file and package name are taken from source
 * TestProjectContext.buildProjectContext()
 *                      .addJavaSources("class Foo{}", "class Bar{}")
 *                      .build();
 *
 * .....
 *
 */
 /* To
 *
 * <p>
 * == Examples
 * <p>
 * === ProjectContext with a JavaSource
 * <p>
 * [source, java]
 * ....
 * ProjectContext context = TestProjectContext.buildProjectContext()
 * .withJavaSources("public class Foo{}")
 * .build();
 * ....
 * <p>
 * <p>
 * === ProjectContext with a JavaSource and classpath
 * <p>
 * [source, java]
 * ....
 * ProjectContext context = TestProjectContext.buildProjectContext()
 * .withJavaSources("public class Foo{}")
 * .withDependencies("com.example:foo:1.0", "groupId2:artifactId:version")
 * .build();
 * ....
 * <p>
 * * A `pom.xml` with a `<dependencies>` section containing the given dependencies will be added.
 * <p>
 * [source,xml]
 * ----
 * <project>
 * <dependencies>
 * <dependency>
 * <groupId>com.example</groupId>
 * <artifactId>foo</artifactId>
 * <version>1.0</version>
 * </dependency>
 * <dependency>
 * <groupId>groupId2</groupId>
 * <artifactId>artifactId</artifactId>
 * <version>version</version>
 * </dependency>
 * </dependencies>
 * </project>
 * ----
 * <p>
 * <p>
 * === with JavaSource and pom.xml
 * <p>
 * [source, java]
 * ....
 * ProjectContext context = TestProjectContext.buildProjectContext()
 * .withJavaSources("public class Foo{}")
 * .withMavenBuildFileSource("<project>...</project>")
 * .build();
 * ....
 * <p>
 * * No additional classpath allowed, all dependencies must be provided in `<dependencies>` section.
 * <p>
 * <p>
 * === with mocked BuildFile
 * <p>
 * [source,java]
 * ....
 * OpenRewriteMavenBuildFile mockedBuildFile = mock(OpenRewriteMavenBuildFile.class);
 * ProjectContext context = TestProjectContext.buildProjectContext()
 * .withMockedBuildFile(mockedBuildFile)
 * .build();
 * ....
 * <p>
 * <p>
 * * The `BuildFileResourceWrapper` will not be executed.
 * * Additional behaviour to successfully run ResourceRegistrars will be added to the mock.
 * [source,java]
 * ....
 * Maven maven = mock(Maven.class);
 * when(mockedBuildFile.getRewriteResource()).thenReturn(maven);
 * when(mockedBuildFile.getAbsoluteProjectDir()).thenReturn(projectRoot);
 * ....
 * <p>
 * <p>
 * TODO: document
 * - Markers and the requirement to have a .git directory
 * - BuildFile, mocked, default
 * - Adding resources
 * - EventPublisher and how to retrieve the created mock
 * - ResourceRegistrars, registered by default, when to add additional registrars
 * - Using the TestProhectContext to initialize a ProjectContext from filesystem resources
 * - Adding dependencies and how they affect the JavaParser
 */
public class TestProjectContext {

    private static final Path DEFAULT_PROJECT_ROOT = Path.of(".").resolve("target").resolve("dummy-test-path").normalize().toAbsolutePath();

    private static final String DEFAULT_PACKAGE_NAME = "not.found";

    /**
     * Build {@code ProjectContext} with default project root of normalized absolute path of './dummy-test-path'.
     * <p>
     * Be aware that application events are not received (publisher is mocked).
     */
    public static Builder buildProjectContext() {
        return new Builder(DEFAULT_PROJECT_ROOT);
    }

    /**
     * Build {@code ProjectContext} with default project root of absolute path of './dummy-test-path'
     * <p>
     * Be aware that @EventListener annotations are not called, {@code eventPublisher} can be provided to verify calls.
     *
     * @param eventPublisher the eventPublisher to use
     */
    public static Builder buildProjectContext(ApplicationEventPublisher eventPublisher) {
        return new Builder(DEFAULT_PROJECT_ROOT, eventPublisher);
    }

    /**
     * @return the default project root dir
     */
    public static Path getDefaultProjectRoot() {
        return DEFAULT_PROJECT_ROOT;
    }

    public static String getDefaultPackageName() { return DEFAULT_PACKAGE_NAME; }

    public static class Builder {
        private Path projectRoot;
        private List<ProjectResourceWrapper> resourceWrapperList = new ArrayList<>();
        private List<String> dependencyCoordinates = new ArrayList<>();
        private Map<Path, String> resources = new LinkedHashMap<>();
        private ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        private ProjectResourceWrapperRegistry resourceWrapperRegistry;
        private OpenRewriteMavenBuildFile mockedBuildFile;
        private DependencyHelper dependencyHelper = new DependencyHelper();
        private SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();

        public Builder(Path projectRoot) {
            this.projectRoot = projectRoot;
            sbmApplicationProperties.setDefaultBasePackage(DEFAULT_PACKAGE_NAME);
        }

        public Builder(Path projectRoot, ApplicationEventPublisher eventPublisher) {
            this(projectRoot);
            this.eventPublisher = eventPublisher;
        }

        public Builder withProjectRoot(Path projectRoot) {
            this.projectRoot = projectRoot.toAbsolutePath().normalize();
            return this;
        }

        public Builder withSbmApplicationProperties(SbmApplicationProperties sbmApplicationProperties) {
            this.sbmApplicationProperties = sbmApplicationProperties;
            return this;
        }

        /**
         * Adds given resource to list of resources which will be parsed and provided through {@code ProjectContext}.
         *
         * @param sourcePath relative from project root.
         * @param content    of the resource
         */
        public Builder addProjectResource(Path sourcePath, String content) {
            if (sourcePath.isAbsolute())
                throw new IllegalArgumentException("Invalid sourcePath given, sourcePath must be given relative from project root.");
            this.resources.put(projectRoot.resolve(sourcePath).toAbsolutePath().normalize(), content);
            return this;
        }

        /**
         * Adds given resource to list of resources which will be parsed and provided through {@code ProjectContext}.
         *
         * @param sourcePathString relative from project root.
         * @param content          of the resource
         */
        public Builder addProjectResource(String sourcePathString, String content) {
            Path sourcePath = Path.of(sourcePathString);
            return addProjectResource(sourcePath, content);
        }

        /**
         * Adds a resource with no content and given path to list of resources which will be parsed and provided through {@code ProjectContext}.
         *
         * @param sourcePathString relative from project root.
         */
        public Builder addEmptyProjectResource(String sourcePathString) {
            addProjectResource(sourcePathString, "");
            return this;
        }

        /**
         * Add a {@code ProjectResourceWrapper} which will be called to map a {@code ProjectResource} to a specialized resource.
         * <p>
         * {@code BuildFileResourceWrapper} and {@code JavaSourceProjectResourceWrapper} are added by default.
         */
        public Builder addRegistrar(ProjectResourceWrapper projectResourceWrapper) {
            resourceWrapperList.add(projectResourceWrapper);
            return this;
        }

        /**
         * Adds the given Java source to the list of compiled Java sources.
         * The source Path is ' src/main/java' in the root module and the path inside is calculated from package declaration if exists.
         */
        @Deprecated
        public Builder addJavaSource(String sourceCode) {
            return withJavaSources(sourceCode);
        }


        public Builder addJavaSource(Path sourcePathDir, String sourceCode) {
            if (sourcePathDir.isAbsolute()) {
                throw new IllegalArgumentException("Source path must be relative to project root dir.");
            }
            String fqName = JavaSourceUtil.retrieveFullyQualifiedClassFileName(sourceCode);
            Path sourcePath = sourcePathDir.resolve(fqName);
            this.resources.put(projectRoot.resolve(sourcePath), sourceCode);
            return this;
        }

        public Builder addJavaSource(String sourcePath, String sourceCode) {
            return addJavaSource(Path.of(sourcePath), sourceCode);
        }

        public Builder addJavaSourceToModule(String modulePath, String sourceCode) {
            if (Path.of(modulePath).isAbsolute()) {
                throw new IllegalArgumentException("Source path must be relative to project root dir.");
            }
            String fqName = JavaSourceUtil.retrieveFullyQualifiedClassFileName(sourceCode);
            Path sourcePath = Path.of(modulePath).resolve("src/main/java").resolve(fqName);
            this.resources.put(projectRoot.resolve(sourcePath), sourceCode);
            return this;
        }


        /**
         * Adds the given Java sources to the list of compiled Java sources.
         * The source Path is ' src/main/java' in the root module and the path inside is calculated from package declaration if exists.
         */
        public Builder withJavaSources(String... sourceCodes) {
            Arrays.asList(sourceCodes).forEach(sourceCode -> {
                String fqName = JavaSourceUtil.retrieveFullyQualifiedClassFileName(sourceCode);
                Path sourcePath = Path.of("src/main/java").resolve(fqName);
//                this.javaSourceDefinitions.put(sourcePath, sourceCode);
                this.resources.put(projectRoot.resolve(sourcePath), sourceCode);
            });

            return this;
        }

        /**
         * Adds the given Java sources to the list of compiled Java sources.
         * The source Path is ' src/test/java' in the root module and the path inside is calculated from package declaration if exists.
         */
        public Builder withJavaTestSources(String... sourceCodes) {
            Arrays.asList(sourceCodes).forEach(sourceCode -> {
                String fqName = JavaSourceUtil.retrieveFullyQualifiedClassFileName(sourceCode);
                Path sourcePath = Path.of("src/test/java").resolve(fqName);
                this.resources.put(projectRoot.resolve(sourcePath), sourceCode);
            });

            return this;
        }

        /**
         * Takes a list of Maven coordinates and creates a pom.xml in project root with these dependencies under compile scope.
         *
         * @param dependencyCoordinate as Maven coordinate, e.g. "groupId:artifactId:version"
         */
        public Builder withBuildFileHavingDependencies(String... dependencyCoordinate) {
            if (containsAnyPomXml() || mockedBuildFile != null)
                throw new IllegalArgumentException("ProjectContext already contains pom.xml files.");
            this.dependencyCoordinates.addAll(Arrays.asList(dependencyCoordinate));
            return this;
        }

        public Builder withMavenRootBuildFileSource(String pomSource) {
            this.resources.put(projectRoot.resolve("pom.xml").normalize(), pomSource);
            return this;
        }

        @Deprecated
        public Builder withMavenBuildFileSource(Path path, String pomSource) {
            this.addProjectResource(projectRoot.resolve(path).normalize(), pomSource);
            return this;
        }

        public Builder withMockedBuildFile(OpenRewriteMavenBuildFile mockedBuildFile) {
            if (containsAnyPomXml() || !dependencyCoordinates.isEmpty())
                throw new IllegalArgumentException("ProjectContext already contains pom.xml files.");
            this.mockedBuildFile = mockedBuildFile;
            return this;
        }

        public Builder withDummyRootBuildFile() {
            if (containsAnyPomXml() || !dependencyCoordinates.isEmpty())
                throw new IllegalArgumentException("ProjectContext already contains pom.xml files.");
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                    "    <modelVersion>4.0.0</modelVersion>\n" +
                    "    <groupId>com.example</groupId>\n" +
                    "    <artifactId>dummy-root</artifactId>\n" +
                    "    <version>0.1.0-SNAPSHOT</version>\n" +
                    "    <packaging>jar</packaging>\n" +
                    "</project>\n";
            resources.put(projectRoot.resolve("pom.xml").normalize(), xml);
            return this;
        }

        public ProjectContext build() {

            // create build files
            verifyValidBuildFileExists();

            // create list of dummy resources
            List<Resource> scannedResources = mapToResources(resources);

            // path scanner should return the dummy resources
//            PathScanner pathScanner = mock(PathScanner.class);
//            when(pathScanner.scan(projectRoot)).thenReturn(scannedResources);

            // precondition verifier should check resources
            // currently ignored and only called by ScanShellCommand
//            PreconditionVerifier preconditionVerifier = mock(PreconditionVerifier.class);
//            PreconditionVerificationResult preconditionVerificationResult = new PreconditionVerificationResult(projectRoot);
//            when(preconditionVerifier.verifyPreconditions(projectRoot, scannedResources)).thenReturn(preconditionVerificationResult);


            // create beans
            ProjectResourceSetHolder projectResourceSetHolder = new ProjectResourceSetHolder();
            JavaRefactoringFactory javaRefactoringFactory = new JavaRefactoringFactoryImpl(projectResourceSetHolder);

            // create ProjectResourceWrapperRegistry and register Java and Maven resource wrapper
            BuildFileResourceWrapper buildFileResourceWrapper = new BuildFileResourceWrapper(eventPublisher);
            resourceWrapperList.add(buildFileResourceWrapper);
            JavaSourceProjectResourceWrapper javaSourceProjectResourceWrapper = new JavaSourceProjectResourceWrapper(javaRefactoringFactory);
            resourceWrapperList.add(javaSourceProjectResourceWrapper);
            orderByOrderAnnotationValue(resourceWrapperList);
            resourceWrapperRegistry = new ProjectResourceWrapperRegistry(resourceWrapperList);

            // create ProjectContextInitializer
            ProjectContextFactory projectContextFactory = new ProjectContextFactory(resourceWrapperRegistry, projectResourceSetHolder, javaRefactoringFactory, new BasePackageCalculator(sbmApplicationProperties));
            ProjectContextInitializer projectContextInitializer = createProjectContextInitializer(projectContextFactory);

            // create ProjectContext
            ProjectContext projectContext = projectContextInitializer.initProjectContext(projectRoot, scannedResources, new RewriteExecutionContext(eventPublisher));

            // replace with mocks
            if (mockedBuildFile != null) {
                // TODO: add javadoc.
                Path pomPath = Path.of("pom.xml");
                when(mockedBuildFile.getSourcePath()).thenReturn(pomPath);
                projectContext.getProjectResources().replace(projectRoot.resolve(pomPath).normalize(), mockedBuildFile);
            }

            return projectContext;
        }

        private void orderByOrderAnnotationValue(List<ProjectResourceWrapper> resourceWrapperList) {
            resourceWrapperList.sort(Comparator.comparing(this::getOrder));
        }

        private Integer getOrder(ProjectResourceWrapper l1) {
            Order annotation = l1.getClass().getAnnotation(Order.class);
            if (annotation != null) {
                return annotation.value();
            }
            return 2147483647;
        }

        @NotNull
        private ProjectContextInitializer createProjectContextInitializer(ProjectContextFactory projectContextFactory) {
            RewriteMavenParserFactory rewriteMavenParserFactory = new RewriteMavenParserFactory(new MavenPomCacheProvider(), eventPublisher);

            GitSupport gitSupport = mock(GitSupport.class);
            when(gitSupport.repoExists(projectRoot.toFile())).thenReturn(true);
            when(gitSupport.getLatestCommit(projectRoot.toFile())).thenReturn(Optional.empty());

            ProjectContextInitializer projectContextInitializer = new ProjectContextInitializer(projectContextFactory, rewriteMavenParserFactory, gitSupport);
            return projectContextInitializer;
        }

        private void verifyValidBuildFileExists() {
            verifyValidBuildFileSetup();

            if (dependencyCoordinates != null && !dependencyCoordinates.isEmpty()) {
                String generatedPomXml = renderPomXmlWithGivenDependencies();
                resources.put(projectRoot.resolve("pom.xml").normalize(), generatedPomXml);
            }

            if (!containsAnyPomXml()) {
                withDummyRootBuildFile();
            }
        }

        private void verifyValidBuildFileSetup() {
            boolean containsRootPom = resources.containsKey(projectRoot.resolve("pom.xml").normalize());
            boolean isClasspathGiven = dependencyCoordinates != null && !dependencyCoordinates.isEmpty();
            boolean isMockedBuildFileGiven = mockedBuildFile != null;

            if (containsRootPom && isClasspathGiven) {
                throw new IllegalArgumentException("Found classpath entries and a root pom.xml in resources. When classpath is provided the root pom gets generated");
            } else if (containsRootPom && isMockedBuildFileGiven) {
                throw new IllegalArgumentException("Found mocked BuildFile and a root pom.xml in resources. When mocked BuildFile is provided no other pom.xml must exist");
            }
            if (mockedBuildFile != null && isClasspathGiven) {
                throw new IllegalArgumentException("Found mocked BuildFile and classpath entries. When mocked BuildFile is provided no other pom.xml must exist");
            }
        }

        private boolean containsAnyPomXml() {
            return resources.keySet().stream().anyMatch(p -> p.toString().endsWith(File.separator + "pom.xml"));
        }

        private List<Resource> mapToResources(Map<Path, String> resources) {
            return resources.entrySet().stream()
                    .map(e -> new TestDummyResource(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
        }

        private Parser.Input createParserInput(Path path, String value) {
            return new Parser.Input(path, () -> new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8)), true);
        }


        @NotNull
        private String renderPomXmlWithGivenDependencies() {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                    "    <modelVersion>4.0.0</modelVersion>\n" +
                    "    <groupId>com.example</groupId>\n" +
                    "    <artifactId>dummy-root</artifactId>\n" +
                    "    <version>0.1.0-SNAPSHOT</version>\n" +
                    "    <packaging>jar</packaging>\n" +
                    "{{dependencies}}\n" +
                    "</project>\n";
            StringBuilder dependenciesSection = new StringBuilder("    ").append("<dependencies>").append("\n");
            dependencyHelper.mapCoordinatesToDependencies(dependencyCoordinates).stream().forEach(dependency -> {
                dependenciesSection.append("    ").append("    ").append("<dependency>").append("\n");
                dependenciesSection.append("    ").append("    ").append("    ").append("<groupId>").append(dependency.getGroupId()).append("</groupId>").append("\n");
                dependenciesSection.append("    ").append("    ").append("    ").append("<artifactId>").append(dependency.getArtifactId()).append("</artifactId>").append("\n");
                dependenciesSection.append("    ").append("    ").append("    ").append("<version>").append(dependency.getVersion()).append("</version>").append("\n");
                dependenciesSection.append("    ").append("    ").append("</dependency>").append("\n");
            });
            dependenciesSection.append("    ").append("</dependencies>").append("\n");
            String buildFileSource = xml.replace("{{dependencies}}", dependenciesSection.toString());

            return buildFileSource;
        }

    }

}
