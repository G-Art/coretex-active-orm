plugins {
    groovy
}
base {
    archivesName.set("struct-processor")
}


repositories {
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://jetbrains.bintray.com/intellij-third-party-dependencies/") }
}

dependencies {

    implementation(gradleApi())
    implementation(localGroovy())
    api(project(":struct-grammar"))

    implementation(group = "org.apache.velocity", name = "velocity", version = "1.7")
    implementation(group = "org.apache.velocity", name = "velocity-tools", version = "2.0")
    implementation(group = "org.postgresql", name = "postgresql", version = "42.2.9")
    implementation(group = "mysql", name = "mysql-connector-java", version = "8.0.25")

    api(group = "org.apache.commons", name = "commons-lang3", version = "3.12.0")
    api(group = "org.apache.commons", name = "commons-text", version = "1.9")
    api(group = "org.apache.commons", name = "commons-collections4", version = "4.4")
    api(group = "org.reflections", name = "reflections", version = "0.9.11")

    testImplementation(group = "org.mockito", name = "mockito-all", version = "1.10.19")
    testImplementation(group = "org.powermock", name = "powermock-module-junit4", version = "2.0.0")
    testImplementation(group = "org.powermock", name = "powermock-api-mockito", version = "1.7.4")

    testImplementation(group = "org.spockframework", name = "spock-core", version = "2.0-groovy-3.0")
    testImplementation(group = "org.hamcrest", name = "hamcrest-core", version = "2.2")
    testImplementation(group = "cglib", name = "cglib", version = "3.3.0")
    testImplementation(group = "cglib", name = "cglib-nodep", version = "3.3.0")
}

tasks.test {
    useJUnitPlatform()
    // listen to events in the test execution lifecycle
    beforeTest(closureOf<TestDescriptor>({
        logger.lifecycle("Running test: ${this}")
    }))

    afterTest(KotlinClosure2<TestDescriptor, TestResult, Unit>({ descriptor, result ->
        logger.lifecycle("Test : ${descriptor} are : ${result.resultType}")
    }))
    // listen to standard out and standard error of the test JVM(s)
    onOutput(KotlinClosure2<TestDescriptor, TestOutputEvent, Unit>({ descriptor, event ->
        logger.lifecycle("Test: ${descriptor} produced standard out/err: ${event.message}")
    }))
}
