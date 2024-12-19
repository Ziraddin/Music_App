// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.8.5"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.0")
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}