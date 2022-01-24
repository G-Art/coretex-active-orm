plugins {
    java
    `java-library`
}

val coretexReleaseCode: String by project
val coretexReleaseVersion: String by project
val coretexReleaseSuffix: String by project
val coretexReleaseName: String by project

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://repo.coretexplatform.com/artifactory/coretex_repo-libs-release/") }
    }
}

subprojects{
    apply { plugin("java") }
    apply { plugin("java-library") }

    group = "com.coretex.active.orm.${coretexReleaseName.toLowerCase()}"
    version = "${coretexReleaseCode}-${coretexReleaseVersion}${if (coretexReleaseSuffix.isNotEmpty()) "-${coretexReleaseSuffix}" else ""}"
    val p = this

    repositories {
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://repo.coretexplatform.com/artifactory/coretex_repo-libs-release/") }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_15
        targetCompatibility = JavaVersion.VERSION_15
    }

    if (this.name != "intellij-coretex-plugin") {
        apply {
            plugin("maven-publish")
        }
        configure<PublishingExtension> {
            publications {
                create<MavenPublication>(p.name) {
                    groupId = p.group.toString()
                    artifactId = p.name
                    version = p.version.toString()
                    from(components["java"])
                }
            }

            repositories {
                maven {
//                repository(url: mavenLocal().getUrl())
                    name = "coretexRepo"
                    url = uri("https://repo.coretexplatform.com/artifactory/coretex_repo-libs-release-local/")
                    credentials(PasswordCredentials::class)
                }
            }
        }

    }


    java {
        sourceCompatibility = JavaVersion.VERSION_15
        targetCompatibility = JavaVersion.VERSION_15
    }
    configurations.implementation.get().isCanBeResolved = true
}

