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
        versionCode = 11
        versionName = "1.0.0"

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
    val androidXCoreKtxVersion = "1.12.0"
    val composeBoomVersion = "2024.02.01"
    val composeActivityVersion = "1.8.2"
    val lifecycleVersion = "2.7.0"
    val hiltVersion = "2.44"
    val hiltComposeNavigationVersion = "1.2.0"
    val navVersion = "2.7.7"
    val roomVersion = "2.6.1"
    val datastoreVersion = "1.0.0"
    val composeLivedataVersion = "1.6.2"
    val firestoreVersion = "24.10.3"
    val ychartsVersion = "2.1.0"
    val mockitoVersion = "3.2.0"
    val coroutinesTestVersion = "1.6.4"
    val coilVersion = "2.5.0"

    implementation("androidx.core:core-ktx:$androidXCoreKtxVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.activity:activity-compose:$composeActivityVersion")
    implementation(platform("androidx.compose:compose-bom:$composeBoomVersion"))
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
    implementation("androidx.hilt:hilt-navigation-compose:$hiltComposeNavigationVersion")
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
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBoomVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
}

kapt {
    correctErrorTypes = true
}