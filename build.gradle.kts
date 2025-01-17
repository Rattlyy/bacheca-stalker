plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"

    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("eu.vendeli.telegram-bot") version "7.8.0"
}

group = "it.rattly"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.kittinunf.fuel:fuel:3.0.0-alpha04")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}