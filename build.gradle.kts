// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val agp_version by extra("4.2.2")
    val agp_version1 by extra("8.2.1")
    val agp_version2 by extra("7.2.0")
    val agp_version3 by extra("8.7.0")
    val agp_version4 by extra("8.6.1")
    val agp_version5 by extra("8.6.1")
    val navigation_version = "2.7.3"
    repositories {
        google()
        mavenCentral()
        maven { url= uri("https://maven.aliyun.com/repository/jcenter" )}
        maven { url = uri("https://jitpack.io") }
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$agp_version5")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${navigation_version}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.2.0")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}

//allprojects {
//    repositories {
//        mavenCentral()
//        google()
//        maven { url= uri("https://maven.aliyun.com/repository/jcenter" )}
//        maven { url = uri("https://jitpack.io") }
//    }
//}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}