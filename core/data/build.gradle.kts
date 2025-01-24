plugins {
    id("cointracker.android.data")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}

// ... rest of dependencies 