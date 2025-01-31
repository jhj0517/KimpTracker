plugins {
    id("cointracker.android.library")
    id("cointracker.android.hilt")
    id("cointracker.android.compose")
}

android {
    namespace = "com.librarydevloperjo.cointracker.feature.home"
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    // Core modules
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
} 