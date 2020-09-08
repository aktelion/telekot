group = "aktelion.telekot"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.3.8-1.4.0-rc")
    implementation("aktelion.telekot:telekot-core:0.1")
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
