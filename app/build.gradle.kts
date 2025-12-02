import java.util.Properties

// Lade die 'local.properties' Datei
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.nurburg_guide"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nurburg_guide"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // API-Schlüssel aus local.properties laden
        val mapApiKey: String = localProperties.getProperty("MAPS_API_KEY") ?: ""
        manifestPlaceholders["MAPS_API_KEY"] = mapApiKey

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // passt zu Kotlin 1.8.20
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

// ⬇️ WICHTIG: jetzt AUßERHALB des android-Blocks!
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin" &&
            requested.name.startsWith("kotlin-stdlib")
        ) {
            // alles auf diese Version zwingen
            useVersion("1.8.20")
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    // Kotlin Stdlib explizit setzen
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")

    // --- Compose / UI ---
    implementation(platform("androidx.compose:compose-bom:2023.06.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.compose.material:material-icons-extended")

    // Google Play Services Dependencies
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:17.0.1")
    implementation("com.google.maps.android:maps-compose:5.0.0")

    // Core
    implementation("androidx.core:core-ktx:1.16.0")

    // --- Netzwerk / Wetter ---
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines: reale Version, passend zu Kotlin 1.8.x
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // --- Lifecycle / ViewModel ---
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")

    // --- Tests ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}