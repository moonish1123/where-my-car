// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    /* this section is for gradle itself */
    repositories {
        google()
        maven("https://maven.fabric.io/public")
    }

    dependencies {
        classpath(libs.hilt.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.devtool.ksp) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.hilt) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.services) apply false
}