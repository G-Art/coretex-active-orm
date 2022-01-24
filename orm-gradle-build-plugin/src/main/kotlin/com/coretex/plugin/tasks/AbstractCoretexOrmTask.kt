package com.coretex.plugin.tasks

import org.gradle.api.Project
import org.gradle.api.internal.ConventionTask
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class AbstractCoretexOrmTask @Inject constructor (
    objectFactory: ObjectFactory
) : ConventionTask() {

    abstract fun customTaskConfiguration(project: Project)

}