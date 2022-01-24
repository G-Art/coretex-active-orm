import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    groovy
    `java-library`
    `embedded-kotlin`
    `kotlin-dsl`
    `java-gradle-plugin`
    kotlin("jvm") version "1.6.10"
}


dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    api(project(":struct-processor"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation(kotlin("stdlib-jdk8"))
}

gradlePlugin {
    plugins {
        create("orm-gradle-build-plugin") {
            id = "com.coretex.orm"
            implementationClass = "com.coretex.plugin.CoretexOrmBuildPlugin"
        }
    }
}

tasks{
    test{
        useJUnitPlatform()
    }
    jar {
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to "Coretex orm build gradle plugin",
                    "Implementation-Version" to archiveVersion,
                    "Specification-Version" to Date()
                )
            )
        }
    }

}
repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://jetbrains.bintray.com/intellij-third-party-dependencies/") }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.11"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.11"
}