plugins {
    id("cointracker.android.library")
    id("cointracker.android.hilt")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.network"
}

dependencies {
    // Retrofit
    implementation(libs.bundles.retrofitBundle)
} 