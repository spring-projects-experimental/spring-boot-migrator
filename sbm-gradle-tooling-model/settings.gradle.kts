pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

rootProject.name = "sbm-gradle-tooling-model"

include("model")
include("plugin")
include("parser")
