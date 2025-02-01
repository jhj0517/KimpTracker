plugins {
    id("cointracker.android.library")
    id("cointracker.android.hilt")
    id("cointracker.android.test")
    id("cointracker.android.coroutines")
    id("cointracker.android.viewmodel")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.viewmodel"
}

dependencies {
    implementation(project(":core:domain"))
} 