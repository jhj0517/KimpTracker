plugins {
    id("cointracker.android.data")
    id("cointracker.android.hilt")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
