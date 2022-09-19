plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    application
    id ("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jooq:joor-java-6:0.9.14")
    implementation("com.caucho:hessian:4.0.38")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass.set("soln.AppKt")
}

tasks.shadowJar {
    minimize()
}
