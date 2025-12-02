// In der root `build.gradle`-Datei

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.13.1")  // Aktualisierte Version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")  // Neuere Kotlin-Plugin-Version
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        //jcenter()  // Optional, je nach Projektstruktur
    }
}