buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.ksp.gradlePlugin)
        classpath(libs.hilt.gradlePlugin)
    }
}

// Empty plugins block since handling everything in buildscript
plugins {}