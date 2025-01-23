plugins {
    id("your.android.library")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.network"
}

dependencies {
    // Retrofit
    implementation(libs.bundles.retrofitBundle)
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
} 