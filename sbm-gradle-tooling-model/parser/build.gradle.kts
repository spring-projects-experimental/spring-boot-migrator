plugins {
    id("org.openrewrite.build.language-library")
}

group = "org.springframework.sbm.gradle.tooling"

repositories {
    if (!project.hasProperty("releasing")) {
        mavenLocal {
            mavenContent {
                excludeVersionByRegex(".+", ".+", ".+-rc-?[0-9]*")
            }
        }

        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }

    mavenCentral {
        mavenContent {
            excludeVersionByRegex(".+", ".+", ".+-rc-?[0-9]*")
        }
    }
}

dependencies {
    implementation(project(":model"))
    implementation("org.openrewrite:rewrite-core:latest.integration")
    implementation("org.openrewrite:rewrite-gradle:latest.integration")
    implementation("org.openrewrite.gradle.tooling:model:latest.integration")
    implementation("org.openrewrite:rewrite-groovy:latest.integration")
    implementation("org.openrewrite:rewrite-hcl:latest.integration")
    implementation("org.openrewrite:rewrite-java:latest.integration")
    implementation("org.openrewrite:rewrite-json:latest.integration")
    implementation("org.openrewrite:rewrite-kotlin:latest.integration")
    implementation("org.openrewrite:rewrite-python:latest.integration")
    implementation("org.openrewrite:rewrite-properties:latest.integration")
    implementation("org.openrewrite:rewrite-protobuf:latest.integration")
    implementation("org.openrewrite:rewrite-xml:latest.integration")
    implementation("org.openrewrite:rewrite-yaml:latest.integration")
    implementation("org.openrewrite.recipe:rewrite-all:latest.integration")
    implementation(gradleApi())
    implementation("org.openrewrite.gradle.tooling:model:latest.integration")
}
