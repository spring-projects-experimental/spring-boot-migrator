# SBM Gradle Project Parser
The project consists of modules:
 - **model** - Defines data structures to be pulled from Gradle Build process via Gradle Tooling API. Also contains utility to query Gradle Build process for a specific model type
 - **plugin** - Registers the `GradelProject` data model builder and has logic for creating a serializable instance of the `GradleProjectData`
 - **parser** - Defines a `GradelProjectParser` which delegates to `DefaultProjectParser` which is very close to Rewrite's `org.openrewrite.gradle.isolated.DefaultProjectParser`which takes `GradleProjectData` obtained from **model** via **plugin** project as a parameter.

Use the `GradelProjectParser` as follows:
```java
GradleProjectParser.parse(new File("/Users/aboyko/Documents/STS4-arm/spring-petclinic"), new File("/Users/aboyko/Documents/STS4-arm/spring-petclinic/build.gradle"), new InMemoryExecutionContext(), new DefaultParserConfig()).collect(Collectors.toList());
```
Parameters:
1. Project root folder
2. Project build script file
3. Rewrite's `ExecutionContext`
4. `ParserConfig` object. (Typically one would use `DefaultParserConfig`)

Before making references to the project and using it as a library it is required to build it:
```bash
./gradlew clean build publishToMavenLocal
```
(It is important to publish to maven local such that when **model** project quries gradle process for the model the plugin is present in the local maven repo)
