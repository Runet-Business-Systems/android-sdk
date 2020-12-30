plugins {
    id("com.android.application")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "ru.rbs.mobile.payment.sample.java"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(group = "", name = "sdk_ui-release", ext = "aar")
    implementation(group = "", name = "sdk_core-release", ext = "aar")
//    implementation(project(":sdk_ui"))
//    implementation(project(":sdk_core"))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.devnied.emvnfccard:library:3.0.1")
    implementation("com.google.android.gms:play-services-wallet:18.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
    implementation("com.caverock:androidsvg-aar:1.4")
    implementation("com.google.android.material:material:1.2.0-beta01")
    implementation("io.card:android-sdk:5.5.1")
}