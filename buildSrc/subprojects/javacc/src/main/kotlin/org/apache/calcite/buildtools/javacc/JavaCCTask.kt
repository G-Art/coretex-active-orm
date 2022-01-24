package org.apache.calcite.buildtools.javacc

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.property

@CacheableTask
open class JavaCCTask @Inject constructor(
    objectFactory: ObjectFactory
) : DefaultTask() {


    @Classpath
    val javaCCClasspath = objectFactory.property<Configuration>()
        .convention(project.configurations.named(JavaCCPlugin.JAVACC_CLASSPATH_CONFIGURATION_NAME))

    @InputFiles
    @PathSensitive(PathSensitivity.NONE)
    // We expect one file only, however there's https://github.com/gradle/gradle/issues/12627
    val inputFile = objectFactory.fileCollection()

    @Input
    val lookAhead = objectFactory.property<Int>().convention(1)

    @Input
    val static = objectFactory.property<Boolean>().convention(false)

    @OutputDirectory
    val output = objectFactory.directoryProperty()
        .convention(project.layout.projectDirectory.dir("src/gen/java"))

    @Input
    val packageName = objectFactory.property<String>()

    @TaskAction
    fun run() {
        project.delete(output.asFileTree)
        project.javaexec {
            classpath = javaCCClasspath.get()
            mainClass.set("javacc")
            args("-STATIC=${static.get()}")
            args("-LOOKAHEAD:${lookAhead.get()}")
            args("-OUTPUT_DIRECTORY:${output.get()}/${packageName.get().replace('.', '/')}")
            args(inputFile.singleFile)
        }


    }
}