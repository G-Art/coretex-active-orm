plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":coretex-active-orm-essential"))
    implementation("org.postgresql:postgresql:42.3.1")

    compileOnly(project(":coretex-active-orm-stub"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}