dependencies {
}

gradlePlugin {
    plugins {
        register("fmpp") {
            id = "calcite.fmpp"
            implementationClass = "org.apache.calcite.buildtools.fmpp.FmppPlugin"
        }
    }
}