plugins {
    java
}

repositories {
    mavenCentral()
    maven(uri("https://repo.coretexplatform.com/artifactory/coretex_repo-libs-release/"))
}

dependencies {
    implementation(project(":coretex-active-orm-essential"))
    implementation("mysql:mysql-connector-java:8.0.27")

    compileOnly(project(":coretex-active-orm-stub"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}