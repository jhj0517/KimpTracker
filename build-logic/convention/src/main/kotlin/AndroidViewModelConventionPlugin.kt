import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidViewModelConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
            }
        }
    }
} 