plugins {
    id("org.jetbrains.intellij") version "1.3.0"
    kotlin("jvm") version "1.5.10"
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":struct-grammar")){
        exclude(group="*")
    }
    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:1.7.2")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.3.1")
    plugins.set(listOf("com.intellij.java"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}

tasks{
    publishPlugin {
        token.set(System.getenv("ORG_GRADLE_PROJECT_intellijPublishToken")) // define environment variable before execute task
    }
    patchPluginXml{
        sinceBuild.set("212.4746")
        untilBuild.set("213.*")
    }
    runIde{
        autoReloadPlugins.set(true)
    }
    test {
        useJUnitPlatform()
    }
}
