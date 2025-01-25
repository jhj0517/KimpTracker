plugins {
    id("cointracker.android.data")
    id("cointracker.android.hilt")
    id("cointracker.android.test")
}

android {
    namespace = "com.librarydevloperjo.cointracker.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
}
