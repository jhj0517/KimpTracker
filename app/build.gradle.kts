plugins {
    id("your.android.application")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "your.app.package"
    defaultConfig {
        applicationId = "your.app.package"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    // Core dependencies
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    // Feature modules
    implementation(project(":feature:home"))
    implementation(project(":feature:info"))
    implementation(project(":feature:kimp"))
    
    // Core modules
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:viewmodel"))

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)
} 