package org.apache.calcite.buildtools.fmpp

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.withGroovyBuilder

@CacheableTask
open class FmppTask @Inject constructor(
    objectFactory: ObjectFactory
) : DefaultTask() {
    @Classpath
    val fmppClasspath = objectFactory.property<Configuration>()
        .convention(project.configurations.named(FmppPlugin.FMPP_CLASSPATH_CONFIGURATION_NAME))

    @InputFile
    @PathSensitive(PathSensitivity.NONE)
    val config = objectFactory.fileProperty()

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    val templates = objectFactory.directoryProperty()

    @InputFile
    @PathSensitive(PathSensitivity.NONE)
    val defaultConfig = objectFactory.fileProperty()
        .convention(templates.file("../default_config.fmpp"))

    @OutputDirectory
    val output = objectFactory.directoryProperty()
        .convention(project.layout.buildDirectory.dir("fmpp/$name"))

    /**
     * Path might contain spaces and TDD special characters, so it needs to be quoted.
     * See http://fmpp.sourceforge.net/tdd.html
     */
    private fun String.tddString() =
        "\"${toString().replace("\\", "\\\\").replace("\"", "\\\"")}\""

    @TaskAction
    fun run() {
        project.delete(output.asFileTree)
        ant.withGroovyBuilder {
            "taskdef"(
                "name" to "fmpp",
                "classname" to "fmpp.tools.AntTask",
                "classpath" to fmppClasspath.get().asPath
            )
            "fmpp"(
                "configuration" to config.get(),
                "sourceRoot" to templates.get().asFile,
                "outputRoot" to output.get().asFile,
                "data" to "tdd(${config.get().toString().tddString()}), " +
                        "default: tdd(${defaultConfig.get().toString().tddString()})"
            )
        }
    }
}