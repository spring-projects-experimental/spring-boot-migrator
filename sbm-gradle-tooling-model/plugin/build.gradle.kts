plugins {
    id("org.openrewrite.build.language-library")
}

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
    implementation("org.openrewrite:rewrite-maven:latest.integration")
    implementation("org.openrewrite:rewrite-gradle:latest.integration")
    implementation("org.openrewrite:rewrite-java:latest.integration")
    implementation("org.openrewrite:rewrite-yaml:latest.integration")
    implementation("org.openrewrite:rewrite-properties:latest.integration")
    implementation("org.openrewrite:rewrite-json:latest.integration")
    implementation("org.openrewrite:rewrite-protobuf:latest.integration")
    implementation("org.openrewrite:rewrite-java-17:latest.integration")
    implementation("org.openrewrite:rewrite-hcl:latest.integration")
    implementation("org.openrewrite:rewrite-python:latest.integration")
    implementation("org.openrewrite.gradle.tooling:model:latest.integration")
    implementation("org.openrewrite:plugin:latest.integration")
    implementation(gradleApi())
}
