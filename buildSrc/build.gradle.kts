plugins {
   `kotlin-dsl`
   `version-catalog`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}
