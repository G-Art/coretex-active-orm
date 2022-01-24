import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `embedded-kotlin`
    `kotlin-dsl` apply false
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}


allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    applyKotlinProjectConventions()
    tasks.withType<AbstractArchiveTask>().configureEach {
        // Ensure builds are reproducible
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        dirMode = "775".toInt(8)
        fileMode = "664".toInt(8)
    }
}
fun Project.applyKotlinProjectConventions() {
    if (project != rootProject) {
        apply(plugin = "org.gradle.kotlin.kotlin-dsl")
    }
}
dependencies {
    subprojects.forEach {
        runtimeOnly(project(it.path))
    }
}