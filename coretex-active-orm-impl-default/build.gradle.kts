plugins {
    java
}

repositories {
    mavenCentral()
    maven(uri("https://repo.coretexplatform.com/artifactory/coretex_repo-libs-release/"))
}

dependencies {
    api(project(":coretex-active-orm-essential"))

    implementation(project(":coretex-active-orm-mysql-dialect"))
    implementation(project(":coretex-active-orm-postgresql-dialect"))

    compileOnly(project(":coretex-active-orm-stub"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}