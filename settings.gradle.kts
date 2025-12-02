pluginManagement {
    repositories {
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.13.1"
        id("org.jetbrains.kotlin.android") version "1.8.20"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Nurburg_Guide"
include(":app")
