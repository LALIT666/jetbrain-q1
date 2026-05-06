plugins {
    kotlin("jvm") version "2.3.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven(url = "https://www.jetbrains.com/intellij-repository/releases")
    maven(url = "https://www.jetbrains.com/intellij-repository/snapshots")
    maven(url = "https://download.jetbrains.com/teamcity-repository")
    maven(url = "https://cache-redirector.jetbrains.com/packages.jetbrains.team/maven/p/grazi/grazie-platform-public")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

dependencies {
    // IDE Starter / Driver stack
    testImplementation("com.jetbrains.intellij.tools:ide-starter-squashed:LATEST-EAP-SNAPSHOT")
    testImplementation("com.jetbrains.intellij.tools:ide-starter-junit5:LATEST-EAP-SNAPSHOT")
    testImplementation("com.jetbrains.intellij.tools:ide-starter-driver:LATEST-EAP-SNAPSHOT")
    testImplementation("com.jetbrains.intellij.driver:driver-client:LATEST-EAP-SNAPSHOT")
    testImplementation("com.jetbrains.intellij.driver:driver-sdk:LATEST-EAP-SNAPSHOT")
    testImplementation("com.jetbrains.intellij.driver:driver-model:LATEST-EAP-SNAPSHOT")

    // JUnit 5
    val junitBom = platform("org.junit:junit-bom:5.12.2")
    testImplementation(junitBom)
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Helpful extras used in JetBrains example project
    testImplementation("org.kodein.di:kodein-di-jvm:7.20.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.10.1")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")

    // FUS reporting
    testImplementation("com.jetbrains.fus.reporting:ap-validation:76")
    testImplementation("com.jetbrains.fus.reporting:model:76")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
    }

    maxHeapSize = "4g"
}

kotlin {
    // tumhare machine par Java 23 hai, isliye फिलहाल 23 use karte hain
    jvmToolchain(23)
}