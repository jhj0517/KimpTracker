import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", platform(libs.findLibrary("okhttp.bom").get()))
                add("implementation", platform(libs.findLibrary("retrofit.bom").get()))
                add("implementation", libs.findLibrary("retrofit").get())
                add("implementation", libs.findLibrary("retrofit.kotlinx.serialization").get())
                add("implementation", libs.findLibrary("okhttp.logging.interceptor").get())
            }
        }
    }
} 