plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

depedencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    implementation("com.android.tools.build:gradle:8.1.1")
}
