plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "ru.rbs.mobile.payment.sample.kotlin"
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
    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }
}

dependencies {
    implementation(group = "", name = "sdk-release", ext = "aar")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
    implementation("com.caverock:androidsvg-aar:1.4")
    implementation("com.google.android.material:material:1.2.0-beta01")
    implementation("io.card:android-sdk:5.5.1")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}