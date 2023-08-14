plugins {
    id("org.openrewrite.build.language-library")
}

dependencies {
    implementation(gradleApi())

    implementation("org.openrewrite.gradle.tooling:model:latest.integration")

}
