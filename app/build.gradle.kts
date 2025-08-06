import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtool.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
}

// 1. local.properties 파일 로드
val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(FileInputStream(localPropertiesFile))
}

fun getLocalProperty(key: String, defaultValue: String = ""): String {
    return properties.getProperty(key, defaultValue)
}

android {
    namespace = "com.brice.wheremycar"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.brice.wheremycar"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/release.keystore")
            storePassword = getLocalProperty("RELEASE_KEY_PASSWORD") as? String
            keyAlias = "whereMyCar"
            keyPassword = getLocalProperty("RELEASE_KEY_PASSWORD") as? String

            println("### RELEASE_KEY_PASSWORD: ${getLocalProperty("RELEASE_KEY_PASSWORD")}")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    hilt {
        enableAggregatingTask = true
    }
    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.material)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.kotlin.coroutines)

    implementation(libs.bundles.logins)

    implementation (platform(libs.androidx.compose.bom))
    implementation(libs.bundles.android.compose)
    implementation(libs.bundles.android.navigation)

    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.android.room)

    implementation (platform(libs.firebase.bom))
    implementation (libs.bundles.firebase)

    /*hilt*/
    implementation(libs.hilt.android)
    implementation(libs.androidx.datastore.core.android)
    ksp(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}