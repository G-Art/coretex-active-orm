plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    api(group = "com.google.guava", name = "guava", version = "28.1-jre")
    api(group = "org.apache.commons", name = "commons-lang3", version = "3.9")
    api(group = "org.apache.commons", name = "commons-collections4", version = "4.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

