import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.android.build.gradle.LibraryExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.api.JavaVersion

class AndroidViewModelConventionPlugin : Plugin<Project> {
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

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
            }
        }
    }
} 