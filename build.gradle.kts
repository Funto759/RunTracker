// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version") //add here.
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.2.0")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.50")
    }
}