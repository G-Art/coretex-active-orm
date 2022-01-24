package org.apache.calcite.buildtools.fmpp

import org.gradle.api.Plugin
import org.gradle.api.Project

open class FmppPlugin : Plugin<Project> {
    companion object {
        const val FMPP_CLASSPATH_CONFIGURATION_NAME = "fmppClaspath"
    }

    override fun apply(target: Project) {
        target.configureFmpp()
    }

    fun Project.configureFmpp() {
        configurations.create(FMPP_CLASSPATH_CONFIGURATION_NAME) {
            isCanBeConsumed = false
        }.defaultDependencies {
            // TODO: use properties for versions
            add(dependencies.create("org.freemarker:freemarker:2.3.29"))
            add(dependencies.create("net.sourceforge.fmpp:fmpp:0.9.16"))
        }
    }
}