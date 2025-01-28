plugins {
    id("cointracker.android.domain")
    id("cointracker.android.hilt")
    id("cointracker.android.test")
    id("cointracker.android.coroutines")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.domain"
}

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
