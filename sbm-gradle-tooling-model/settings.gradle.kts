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

plugins {
    id("com.gradle.enterprise") version "latest.release"
    id("com.gradle.common-custom-user-data-gradle-plugin") version "latest.release"
}

gradleEnterprise {
    val isCiServer = System.getenv("CI")?.equals("true") ?: false
    server = "https://ge.openrewrite.org/"
    val gradleCacheRemoteUsername: String? = System.getenv("GRADLE_ENTERPRISE_CACHE_USERNAME")
    val gradleCacheRemotePassword: String? = System.getenv("GRADLE_ENTERPRISE_CACHE_PASSWORD")

    buildCache {
        remote(HttpBuildCache::class) {
            url = uri("https://ge.openrewrite.org/cache/")
            isPush = isCiServer
            if (!gradleCacheRemoteUsername.isNullOrBlank() && !gradleCacheRemotePassword.isNullOrBlank()) {
                credentials {
                    username = gradleCacheRemoteUsername
                    password = gradleCacheRemotePassword
                }
            }
        }
    }

    buildScan {
        capture {
            isTaskInputFiles = true
        }

        isUploadInBackground = !isCiServer

        publishAlways()
        this as com.gradle.enterprise.gradleplugin.internal.extension.BuildScanExtensionWithHiddenFeatures
        publishIfAuthenticated()
    }

}
