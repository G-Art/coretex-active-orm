package org.apache.calcite.buildtools.javacc


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

open class JavaCCPlugin : Plugin<Project> {
    companion object {
        const val JAVACC_CLASSPATH_CONFIGURATION_NAME = "javaccClaspath"
        const val GENERATE_SOURCES_TASK_NAME = "generateSources"
    }

    override fun apply(target: Project) {
        target.configureJavaCC()

        val java = target.extensions.getByType(JavaPluginExtension::class)
        val main = java.sourceSets["main"]
        main?.java?.srcDir("src/gen/java")
    }

    fun Project.configureJavaCC() {
        configurations.create(JAVACC_CLASSPATH_CONFIGURATION_NAME) {
            isCanBeConsumed = false
        }.defaultDependencies {
            // TODO: use properties for versions
            add(dependencies.create("net.java.dev.javacc:javacc:4.0")) // 7.0.5"))
        }

        tasks.register(GENERATE_SOURCES_TASK_NAME) {
            description = "Generates sources (e.g. JavaCC)"
            dependsOn(tasks.withType<JavaCCTask>())
        }
    }
}