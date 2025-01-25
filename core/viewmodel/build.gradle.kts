plugins {
    id("cointracker.android.library")
    id("cointracker.android.hilt")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.viewmodel"
}

dependencies {
    implementation(project(":core:domain"))
    
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.kotlinx.coroutines.android)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
} 