package com.coretex.plugin.extension

import com.coretex.Constants
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class CoretexOrmBuildExtension
@Inject
constructor(objectFactory: ObjectFactory) {

    @Input
    @Optional
    val genPackage = objectFactory.property<String>()
        .convention(Constants.DEFAULT_GEN_PACKAGE)

    @Input
    @Optional
    val dbDialect = objectFactory.property<String>()
        .convention(Constants.DEFAULT_DB_DIALECT)
}