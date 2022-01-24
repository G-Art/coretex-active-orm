plugins {
    java
    calcite.fmpp
    calcite.javacc
}

repositories {
    mavenCentral()
    maven(uri("https://repo.coretexplatform.com/artifactory/coretex_repo-libs-release/"))
}

dependencies {
    api(group = "org.apache.calcite", name = "calcite-core", version = "1.28.0")

    api(group = "org.apache.metamodel", name = "MetaModel-jdbc", version = "5.3.1")

    api(group = "org.springframework", name = "spring-jdbc", version = "5.3.14")

    api(project(":coretex-active-orm-common"))

    compileOnly(project(":coretex-active-orm-stub"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val fmppMain by tasks.registering(org.apache.calcite.buildtools.fmpp.FmppTask::class) {
    config.set(file("src/main/codegen/config.fmpp"))
    templates.set(file("src/main/codegen/templates"))
}

val javaCCMain by tasks.registering(org.apache.calcite.buildtools.javacc.JavaCCTask::class) {
    dependsOn(fmppMain)
    val parserFile = fmppMain.map {
        it.output.asFileTree.matching { include("**/Parser.jj") }
    }
    inputFile.from(parserFile)
    packageName.set("com.coretex.orm.core.query.parser")
}

tasks {
    compileJava {
        dependsOn(javaCCMain)
    }
}
