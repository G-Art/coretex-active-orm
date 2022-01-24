dependencies {
}

gradlePlugin {
    plugins {
        register("javacc") {
            id = "calcite.javacc"
            implementationClass = "org.apache.calcite.buildtools.javacc.JavaCCPlugin"
        }
    }
}