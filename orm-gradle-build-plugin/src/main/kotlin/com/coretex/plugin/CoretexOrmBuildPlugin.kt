package com.coretex.plugin

import com.coretex.plugin.CoretexOrmPluginConstants.EXTENSION_NAME
import com.coretex.plugin.CoretexOrmPluginConstants.GENERATE_TASK_NAME
import com.coretex.plugin.CoretexOrmPluginConstants.INIT_TASK_NAME
import com.coretex.plugin.CoretexOrmPluginConstants.PARSE_TASK_NAME
import com.coretex.plugin.CoretexOrmPluginConstants.UPDATESYSTEM_TASK_NAME
import com.coretex.plugin.extension.CoretexOrmBuildExtension
import com.coretex.plugin.tasks.*
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import kotlin.reflect.KClass

open class CoretexOrmBuildPlugin : AbstractCoretexOrmBuildPlugin() {

    override fun tasksForConfiguration(): Map<String, KClass<out AbstractCoretexOrmTask>> {
        return mapOf(
            GENERATE_TASK_NAME to GenerateStructItemsTask::class,
            PARSE_TASK_NAME to ParseStructItemsTask::class,
            INIT_TASK_NAME to InitializeItemsMetaDataTask::class,
            UPDATESYSTEM_TASK_NAME to UpdateItemsMetaDataTask::class
        )
    }

    override fun apply(project: Project) {

        checkGradleVersion(project)


        val extension = project.extensions.create(
            EXTENSION_NAME,
            CoretexOrmBuildExtension::class
        )
        configureTasks(project, extension)

    }


}