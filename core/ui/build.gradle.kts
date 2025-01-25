plugins {
    id("cointracker.android.library")
    id("cointracker.android.hilt")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.ui"
}

dependencies {
    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(project(":core:domain"))
}

// ... rest of dependencies 