package com.coretex.plugin.tasks

import com.coretex.build.services.impl.JavaSourceFileGeneratorService
import com.coretex.build.services.impl.SqlSchemaFileGeneratorService
import com.coretex.plugin.CoretexOrmPluginConstants.GENERATE_TASK_NAME
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPlugin.COMPILE_JAVA_TASK_NAME
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.apply
import javax.inject.Inject

open class GenerateStructItemsTask @Inject constructor(objectFactory: ObjectFactory) :
    AbstractCoretexOrmTask(objectFactory) {

    private val javaFileGeneratorService = JavaSourceFileGeneratorService()

    private val sqlFileGeneratorService = SqlSchemaFileGeneratorService()

    @TaskAction
    fun exec() {
        logger.lifecycle("Generate items...")

        javaFileGeneratorService.generate()

        sqlFileGeneratorService.generate()

        logger.lifecycle("...all items has been generated.")
    }

    override fun customTaskConfiguration(project: Project) {
        project.plugins.apply(JavaPlugin::class)
        project.tasks.findByName(COMPILE_JAVA_TASK_NAME)?.dependsOn(GENERATE_TASK_NAME)
    }
}