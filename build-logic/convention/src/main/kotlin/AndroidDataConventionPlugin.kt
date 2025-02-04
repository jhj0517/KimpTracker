import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies

class AndroidDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = 34
                
                defaultConfig {
                    targetSdk = 34
                    minSdk = 24
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            dependencies {
                add("implementation", project.dependencies.platform(libs.findLibrary("okhttp.bom").get()))
                add("implementation", project.dependencies.platform(libs.findLibrary("retrofit.bom").get()))
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
            }
        }
    }
} 