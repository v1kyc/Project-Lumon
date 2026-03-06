apply(plugin = "application")

configure<JavaApplication> {
    mainClass.set("cc.viky.lumon.example.Main")
}

dependencies {
    implementation(project(":lumon-core"))
}