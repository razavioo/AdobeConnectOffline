import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    application
}

group = "dev.razavioo.adobe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.squareup.okio:okio-multiplatform:3.0.0-alpha.9")

    val xmlVersion = "0.8.13"
    implementation("com.tickaroo.tikxml:annotation:$xmlVersion")
    @Suppress("GradlePackageUpdate")
    implementation("com.tickaroo.tikxml:core:$xmlVersion")
    @Suppress("GradlePackageUpdate")
    kapt("com.tickaroo.tikxml:processor:$xmlVersion")

    implementation("net.bramp.ffmpeg:ffmpeg:0.6.2")
    implementation("org.slf4j:slf4j-api:1.7.36")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}