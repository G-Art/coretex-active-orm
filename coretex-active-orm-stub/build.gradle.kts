plugins {
    java
}


repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":coretex-active-orm-common"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}