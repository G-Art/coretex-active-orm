package com.coretex.plugin.tasks

import com.coretex.Constants.PROJECT
import com.coretex.Constants.SQL_SCHEMA_FILE_NAME
import com.coretex.build.context.CoretexPluginContext
import com.coretex.plugin.CoretexOrmPluginConstants.GENERATE_TASK_NAME
import com.coretex.plugin.CoretexOrmPluginConstants.INIT_TASK_NAME
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class InitializeItemsMetaDataTask @Inject constructor(objectFactory: ObjectFactory) :
    AbstractCoretexOrmTask(objectFactory) {
    @TaskAction
    fun exec() {
        val context = CoretexPluginContext.instance
        val p: Project = context.getBuildProperty(PROJECT) as Project
        val tmpDirPath = "${p.projectDir.absolutePath}/tmp/sql/${SQL_SCHEMA_FILE_NAME}"
        val sqlFile = File(tmpDirPath)
        if (sqlFile.exists()) {
            logger.lifecycle("Initialization has been started...")
            val initService = context.dbDialect.dbService
            logger.lifecycle("Db[${context.getBuildProperty("db.url")}]@${context.getBuildProperty("db.user.name")}")
            initService.initDb(
                context.getBuildProperty("db.url") as String?,
                context.getBuildProperty("db.user.name") as String?,
                context.getBuildProperty("db.user.password") as String?,
                sqlFile
            )

            logger.lifecycle("Initialization finished")

        } else {
            logger.lifecycle("File [$tmpDirPath] is not exist!!!")
        }
    }


    override fun customTaskConfiguration(project: Project) {
        project.tasks.findByName(INIT_TASK_NAME)?.dependsOn(GENERATE_TASK_NAME)
    }

}