plugins {
    groovy
}
base {
    archivesName.set("common-grammar")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://jetbrains.bintray.com/intellij-third-party-dependencies/") }
}

sourceSets {
    main {
        resources {
            srcDirs.add(file("resources"))
        }
    }
}

dependencies {

    api(group = "com.intellij", name = "util", version = "1.0_coretex")
    api(group = "com.intellij", name = "extensions", version = "1.0_coretex")
    api(group = "com.intellij", name = "platform-api", version = "1.0_coretex")
    api(group = "com.intellij", name = "platform-core-ui", version = "1.0_coretex")
    api(group = "com.intellij", name = "platform-impl", version = "1.0_coretex")
    api(group = "com.intellij", name = "platform-util-ui", version = "1.0_coretex")
    api(group = "com.intellij", name = "openapi", version = "1.0_coretex")
    api(group = "com.intellij", name = "intellij-fastutil", version = "1.0_coretex")
    api(group = "com.intellij", name = "light-psi-all", version = "1.5.1_coretex")
    api("com.github.jetbrains:grammar-kit:2021.1.2") {
        exclude(group = "org.jetbrains.plugins")
        exclude(module = "idea")
    }

    api(group = "com.github.hurricup",name = "jflex", version ="1.7.0")
    api(group = "org.jetbrains", name = "annotations", version = "19.0.0")
    api(group = "org.picocontainer", name = "picocontainer", version = "2.15")
    api(group = "com.github.adedayo.intellij.sdk", name = "asm-all", version = "142.1")
    api(group = "dk.brics", name = "automaton", version = "1.12-1")
    api(group = "org.jetbrains.trove4j", name = "trove4j", version = "20160824")

    api(group = "com.google.guava", name = "guava", version = "28.2-jre")

    testImplementation(group = "junit", name = "junit", version = "4.12")

}
tasks {

    val generateLexer = register<JavaExec>("generateLexer") {
        description = "Generates lexer sources"
        mainClass.set("jflex.Main")

        val flex = project.file("resources/struct.flex").absolutePath

        args = listOf("-d", "src/main/java/com/coretex/struct/gen/parser/lexer",
                "-skel", project.file("resources/idea-flex.skeleton").absolutePath,
                flex
        )

        classpath = configurations["implementation"]
    }.get()

    val generateParser = register<JavaExec>("generateParser") {
        dependsOn(generateLexer)
        description = "Generates parser sources with GrammarKit."
        mainClass.set("org.intellij.grammar.Main")

        val bnf = project.file("resources/struct.bnf").absolutePath

        args = listOf("src/main/java", bnf)

        classpath = configurations["implementation"]
    }.get()

    val generateSources = register("generateSources") {
        dependsOn(generateParser)
    }.get()

    clean {
        delete.add("src/main/java/com/coretex/struct/gen")
    }

    compileJava {
        dependsOn(generateSources)
    }
}



