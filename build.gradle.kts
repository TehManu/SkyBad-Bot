plugins {
    id("application")
    id("com.gradleup.shadow") version("8.3.6")
}

application.mainClass = "dev.tehmanu.skybad.SkyBad"
group = "dev.tehmanu"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(libs.jda) {
        exclude(module = "opus-java")
        exclude(module = "tink")
    }

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    // Set this to the version of java you want to use,
    // the minimum required for JDA is 1.8
    sourceCompatibility = "21"
}