import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"
    id("com.bnorm.robocode") version "0.1.0"
    application
}

version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations.implementation.configure {
    extendsFrom(configurations.robocode.get())
    extendsFrom(configurations.robocodeRuntime.get())
}

dependencies {
    implementation("com.jakewharton.picnic:picnic:0.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xopt-in=kotlin.ExperimentalStdlibApi"
        )
    }
}

tasks.run.configure { dependsOn(tasks.robocodeDownload) }
application {
    mainClass.set("MainKt")
}
