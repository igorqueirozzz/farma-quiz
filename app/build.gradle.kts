import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.github.triplet.play")
}

val keystorePropertiesFile = rootProject.file("app/keystore.properties")
val keyStoreProperties = Properties()
keyStoreProperties.load(FileInputStream(keystorePropertiesFile))



android {
    namespace = "dev.queiroz.farmaquiz"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.queiroz.farmaquiz"
        minSdk = 26
        targetSdk = 33
        versionCode = 7
        versionName = "1.0.1-Beta"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        signingConfigs {
            create("release") {
                keyAlias = keyStoreProperties["keyAlias"] as String
                keyPassword = keyStoreProperties["keyPassword"] as String
                storeFile = file(keyStoreProperties["storeFile"] as String)
                storePassword = keyStoreProperties["storePassword"] as String
            }
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs["release"]

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

play {
    defaultToAppBundles.set(true)
    serviceAccountCredentials.set(file("farma-quiz-406212-7fcf675014c9.json"))
}

dependencies {
    val lifecycleVersion = "2.6.1"
    val hiltVersion = "2.44"
    val navVersion = "2.7.3"
    val roomVersion = "2.4.1"
    val datastoreVersion = "1.0.0"
    val composeLivedataVersion = "1.5.3"
    val firestoreVersion = "24.8.1"
    val ychartsVersion = "2.1.0"
    val mockitoVersion = "3.2.0"
    val coroutinesTestVersion = "1.5.0"
    val coilVersion = "2.5.0"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeLivedataVersion")
    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.5.2")
    // Navigation
    implementation("androidx.navigation:navigation-compose:$navVersion")
    implementation("androidx.navigation:navigation-testing:$navVersion")
    // Room Database
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // Data Store
    implementation("androidx.datastore:datastore-preferences:$datastoreVersion")
    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore-ktx:$firestoreVersion")
    // YCharts
    implementation("co.yml:ycharts:$ychartsVersion")
    // Coil
    implementation("io.coil-kt:coil-compose:$coilVersion")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
}

kapt {
    correctErrorTypes = true
}