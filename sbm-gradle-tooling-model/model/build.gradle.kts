plugins {
    id("java")
    id("nebula.maven-resolved-dependencies") version "18.4.0"
    id("com.gradle.plugin-publish") version "1.1.0"
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
    implementation(gradleApi())

    implementation("org.openrewrite.gradle.tooling:model:latest.integration")

}
