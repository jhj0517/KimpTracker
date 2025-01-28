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
    compileOnly(libs.hilt.gradlePlugin)
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
        register("androidHilt") {
            id = "cointracker.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidTest") {
            id = "cointracker.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidCoroutines") {
            id = "cointracker.android.coroutines"
            implementationClass = "AndroidCoroutinesConventionPlugin"
        }
    }
} 