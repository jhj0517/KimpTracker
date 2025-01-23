plugins {
    id("your.android.library")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.librarydevloperjo.cointracker.feature.home"
}

dependencies {
    // Core modules
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    
    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
} 