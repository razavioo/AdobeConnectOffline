import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
    application
}

group = "dev.emad.adobe"
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

    implementation("com.github.kokorin.jaffree:jaffree:2021.12.30")
    implementation("org.slf4j:slf4j-api:1.7.36")

    val jsoupVersion = "1.14.3"
    implementation("org.jsoup:jsoup:$jsoupVersion")

    val ktorVersion = "2.0.0"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    val logbackVersion = "1.2.11"
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
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