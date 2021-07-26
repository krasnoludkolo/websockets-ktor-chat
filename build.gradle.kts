 plugins {
    kotlin("jvm") version "1.5.20"
    id("com.github.johnrengelman.shadow") version  "7.0.0"
}

group = "com.github.krasnoludkolo"
version = "1.0.0"

repositories {
    mavenCentral()
}

val ktor_version: String by project
val logback_version: String by project

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
}

 tasks {
     named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
         archiveBaseName.set("ws-chat")
         mergeServiceFiles()
         manifest {
             attributes(mapOf("Main-Class" to "com.github.krasnoludkolo.chat.server.ApplicationKt"))
         }
     }
 }

 tasks {
     build {
         dependsOn(shadowJar)
     }
 }