pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

    }
}

rootProject.name = "KimpTracker"

// Temporarily comment out app module
// include(":app")

// Feature modules
include(":feature:home")
include(":feature:info")

// Core modules
include(":core:ui")
include(":core:data")
include(":core:domain")
include(":core:network")
include(":core:viewmodel")