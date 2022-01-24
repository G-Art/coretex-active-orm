package com.coretex.plugin.tasks

import com.coretex.build.builder.impl.ItemsBuilderImpl
import com.coretex.plugin.CoretexOrmPluginConstants.GENERATE_TASK_NAME
import com.coretex.plugin.CoretexOrmPluginConstants.PARSE_TASK_NAME
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class ParseStructItemsTask @Inject constructor(objectFactory: ObjectFactory): AbstractCoretexOrmTask(objectFactory) {

    @TaskAction
    fun exec() {
        logger.lifecycle("Parse items structs...")

        ItemsBuilderImpl().parse()

        logger.lifecycle("items parsed...")
    }

    override fun customTaskConfiguration(project: Project) {
        project.tasks.findByName(GENERATE_TASK_NAME)?.dependsOn(PARSE_TASK_NAME)
    }
}