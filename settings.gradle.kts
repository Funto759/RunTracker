import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.mavenCentral

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven { url= uri("https://maven.aliyun.com/repository/jcenter" )}
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Run Tracker"
include(":app")
 