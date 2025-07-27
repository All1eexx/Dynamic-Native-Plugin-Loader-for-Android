plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.all1eexxx.dynamicnativepluginloaderforandroid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.all1eexxx.dynamicnativepluginloaderforandroid"
        minSdk = 21
        targetSdk = 36
        versionCode = 1131
        versionName = "1.1.3.1"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndkVersion = "29.0.13599879"

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        androidResources {
            @Suppress("UnstableApiUsage")
            localeFilters.add("en")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "36.0.0"
    ndkVersion = "29.0.13599879"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}