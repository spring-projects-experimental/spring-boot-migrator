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
    implementation("org.openrewrite.gradle.tooling:model:latest.integration")
    implementation("org.openrewrite:rewrite-maven:latest.integration")
    implementation(gradleApi())
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}
