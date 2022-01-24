package com.coretex.plugin

import com.coretex.Constants.*
import com.coretex.build.context.CoretexPluginContext
import com.coretex.common.DbDialect
import com.coretex.plugin.extension.CoretexOrmBuildExtension
import com.coretex.plugin.tasks.AbstractCoretexOrmTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME
import org.gradle.api.plugins.PluginInstantiationException
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import java.lang.Runtime.Version.parse
import kotlin.reflect.KClass

abstract class AbstractCoretexOrmBuildPlugin : Plugin<Project> {

    private val context: CoretexPluginContext = CoretexPluginContext.instance

    protected fun checkGradleVersion(project: Project) {
        if (parse(project.gradle.gradleVersion).feature() < 7) {
            throw PluginInstantiationException("build-gradle-plugin requires Gradle 7 and higher")
        }
    }

    protected fun configureTasks(project: Project, extension: CoretexOrmBuildExtension) {
        info("configuration", "Configuring plugin")
        init(project)
        tasksForConfiguration().forEach { (name, t) ->
            val task: AbstractCoretexOrmTask = project.tasks.create(name, t)
            task.customTaskConfiguration(project)
        }

        project.configurations.findByName(IMPLEMENTATION_CONFIGURATION_NAME)?.isCanBeResolved = true
    }

    protected fun init(project: Project) {
        context.cleanUpContext()
        defineConfig(project)
    }

    private fun defineConfig(project: Project) {
        val coretexOrmBuildExtension = project.extensions.findByType(CoretexOrmBuildExtension::class)

        context.addProperty(System.getProperties())
        context.addProperty(PROPERTY_MODULE_FOR_GEN_MODEL, project.name)
        context.addProperty(
            PROPERTY_GEN_JAVA_SRC,
            "${project.projectDir.absolutePath}${PATH_SEPARATOR}src${PATH_SEPARATOR}gen${PATH_SEPARATOR}java${PATH_SEPARATOR}${
                coretexOrmBuildExtension?.genPackage?.get()?.replace("\\.", PATH_SEPARATOR)
            }"
        )
        context.addProperty(PROJECT, project)

        context.setDbDialect(DbDialect.fromString(coretexOrmBuildExtension?.dbDialect?.get()))
        info(null,"*************  Coretex orm context config *****************")
        info(null,"*")
        info(null,"* Db dialect :: ${context.dbDialect.name}")
        context.allBuildProperties.forEach { (k, v) ->
            info(null, "* $k :: $v")
        }
        info(null,"*************  Coretex orm context config *****************")
    }


    abstract fun tasksForConfiguration(): Map<String, KClass<out AbstractCoretexOrmTask>>

    fun error(logCategory: String? = null, message: String, e: Throwable? = null) =
        log(LogLevel.ERROR, logCategory, message, e)

    fun warn(logCategory: String? = null, message: String, e: Throwable? = null) =
        log(LogLevel.WARN, logCategory, message, e)

    fun info(logCategory: String? = null, message: String, e: Throwable? = null) =
        log(LogLevel.INFO, logCategory, message, e)

    fun debug(logCategory: String? = null, message: String, e: Throwable? = null) =
        log(LogLevel.DEBUG, logCategory, message, e)

    private fun log(level: LogLevel, logCategory: String?, message: String, e: Throwable?) {
        val category = "build-gradle-plugin :: ${logCategory ?: ""}".trim()
        val logger = Logging.getLogger(AbstractCoretexOrmBuildPlugin::class.java)
        if (e != null && level != LogLevel.ERROR && !logger.isDebugEnabled) {
            logger.log(level, "[$category] $message. Run with --debug option to get more log output.")
        } else {
            logger.log(level, "[$category] $message", e)
        }
    }

}