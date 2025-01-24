plugins {
    `kotlin-dsl`
}

group = "com.librarydevloperjo.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "cointracker.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "cointracker.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidDomain") {
            id = "cointracker.android.domain"
            implementationClass = "AndroidDomainConventionPlugin"
        }
        register("androidData") {
            id = "cointracker.android.data"
            implementationClass = "AndroidDataConventionPlugin"
        }
    }
} 