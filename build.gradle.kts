plugins {
    kotlin("jvm") version "1.9.0"
    application
}

repositories {
    mavenCentral()
}

val javafxVersion = "21"

// Automatically detect platform for JavaFX native libraries
val osName = System.getProperty("os.name")
val platform = when {
    osName.startsWith("Windows") -> "win"
    osName.startsWith("Mac") -> "mac"
    else -> "linux"
}

dependencies {
    val javafxVersion = "21"
    implementation("org.openjfx:javafx-controls:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-base:$javafxVersion:$platform")
}

application {
    mainClass.set("MainKt") // Update if your main class is named differently
}

kotlin {
    jvmToolchain(17)
}