group = "aktelion.telekot"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm")
    `maven-publish`
    `java-library`
}

apply(plugin = "java-library")

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

apply(plugin = "org.gradle.maven-publish")

publishing {
    publications {
        create<MavenPublication>("telekot-core") {
            artifactId = "telekot-core"
            version = "0.1"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.8.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.3.8-1.4.0-rc")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_13
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "13"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "13"
    }
}
