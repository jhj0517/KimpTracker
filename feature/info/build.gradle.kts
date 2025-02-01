plugins {
    id("cointracker.android.library")
    id("cointracker.android.hilt")
    id("cointracker.android.compose")
}

android {
    namespace = "com.librarydevloperjo.cointracker.feature.info"
}

dependencies {
    // Core modules
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
}
