plugins {
    id("cointracker.android.library")
    id("cointracker.android.hilt")
    id("cointracker.android.test")
    id("cointracker.android.coroutines")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.viewmodel"
}

dependencies {
    implementation(project(":core:domain"))
    
    implementation(libs.androidx.lifecycle.viewModelCompose)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
} 