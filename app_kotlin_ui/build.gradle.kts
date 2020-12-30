plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("plugin.serialization")
    id("kotlin-android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "ru.rbs.mobile.payment.sample.kotlin"
        minSdkVersion(21)
        targetSdkVersion(30)
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(group = "", name = "sdk_ui-release", ext = "aar")
    implementation(group = "", name = "sdk_core-release", ext = "aar")
    implementation(group = "", name = "sdk_threeds-release", ext = "aar")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Example
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
    implementation("io.ktor:ktor-client-core:1.4.0")
    implementation("io.ktor:ktor-client-android:1.4.0")
    implementation("io.ktor:ktor-client-json:1.4.0")
    implementation("io.ktor:ktor-client-serialization:1.4.0")
    implementation("io.ktor:ktor-client-logging:1.4.0")
    implementation("org.slf4j:slf4j-simple:1.7.26")

    // SDK
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.devnied.emvnfccard:library:3.0.1")
    implementation("com.caverock:androidsvg-aar:1.4")
    implementation("io.card:android-sdk:5.5.1")
    implementation("com.google.android.gms:play-services-wallet:18.0.0")

    // 3DS2 SDK
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.squareup.okhttp3:okhttp:3.11.0")
    implementation("com.google.android.gms:play-services-ads:17.2.1")
    implementation("com.google.android.gms:play-services-location:16.0.0")

    implementation("com.google.android.material:material:1.2.0-beta01")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.1")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}