plugins {
    id("org.openrewrite.build.root") version("latest.release")
}


allprojects {
    group = "org.springframework.sbm.gradle.tooling"
    description = "A model for extracting semantic information out of Gradle build files necessary for refactoring them."
}
