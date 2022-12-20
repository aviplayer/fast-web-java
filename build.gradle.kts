import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    kotlin("jvm") version "1.7.20"
    application
    kotlin("kapt") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("org.graalvm.buildtools.native") version "0.9.0"
}

group = "org.fasthttp"
version = "1.0-SNAPSHOT"

repositories {
    maven { url = uri("https://repo.spring.io/libs-snapshot")}
    mavenCentral()
}

dependencies {
    implementation(platform("io.projectreactor:reactor-bom:2022.0.0-RC1"))
    implementation("io.projectreactor.netty:reactor-netty-core")
    implementation("io.projectreactor.netty:reactor-netty-http")

    implementation("com.ongres.scram:common:2.1")
    implementation("com.ongres.scram:client:2.1")
    implementation("io.vertx:vertx-pg-client:4.3.5")

    // native image fixes
    compileOnly("io.netty:netty-transport-native-epoll:4.1.84.Final")
    //implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.1")

    // Serialization
    implementation("com.dslplatform:dsl-json-java8:1.9.9")
    kapt("com.dslplatform:dsl-json-java8:1.9.9")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

gradle.taskGraph.whenReady {
    allTasks
        .filter { it.hasProperty("duplicatesStrategy") }
        .forEach {
            it.setProperty("duplicatesStrategy", "EXCLUDE")
        }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
}


application {
    mainClass.set("com.fasthttp.MainKt")
}

nativeBuild {
    imageName.set("app")
    mainClass.set("com.fasthttp.MainKt")
    verbose.set(true)
    fallback.set(false)
    buildArgs.set(listOf(
    ))
}