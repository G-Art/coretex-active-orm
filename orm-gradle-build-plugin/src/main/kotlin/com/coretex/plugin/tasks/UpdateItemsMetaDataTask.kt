package com.coretex.plugin.tasks

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.strategies.Resolution
import com.coretex.plugin.CoretexOrmPluginConstants.GENERATE_TASK_NAME
import com.coretex.plugin.CoretexOrmPluginConstants.UPDATESYSTEM_TASK_NAME
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.sql.Connection
import java.util.function.Function
import java.util.stream.Stream
import javax.inject.Inject

open class UpdateItemsMetaDataTask @Inject constructor(objectFactory: ObjectFactory) :
    AbstractCoretexOrmTask(objectFactory) {

    @Internal
    val context: CoretexPluginContext = CoretexPluginContext.instance

    @TaskAction
    fun exec() {
        val initService = context.dbDialect.dbService

        initService.updateDb(
            context.getBuildProperty("db.url") as String,
            context.getBuildProperty("db.user.name") as String,
            context.getBuildProperty("db.user.password") as String
        ) { createDiffResolution(it) }
    }

     private fun createDiffResolution(connection: Connection): Stream<Resolution> {
        val dbDiffService = context.dbDialect.dbDiffService()

        return dbDiffService.defineItemDiffHolder(connection)
            .map(dbDiffService::resolve)
            .filter({ !it.commands.isEmpty() })
    }


    override fun customTaskConfiguration(project: Project) {
        project.tasks.findByName(UPDATESYSTEM_TASK_NAME)?.dependsOn(GENERATE_TASK_NAME)
    }

}