plugins {
    id("cointracker.android.application")
    id("cointracker.android.hilt")
    id("cointracker.android.application.compose")
}

android {
    namespace = "com.librarydevloperjo.cointracker"
    defaultConfig {
        applicationId = "com.librarydevloperjo.cointracker"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    // Only essential dependencies
    implementation(libs.androidx.activity.compose)
    
    // Feature modules
    implementation(project(":feature:home"))
    
    // Core modules - only what's needed for HomeScreen
    implementation(project(":core:ui"))
} 