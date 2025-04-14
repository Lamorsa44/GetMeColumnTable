plugins {
    java
    application
}

application {
    mainClass = "src/main/java/org/example/Main.java"
    mainModule = "src/main"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.19.1")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "org.example.Main"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
}