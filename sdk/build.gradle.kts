import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("io.gitlab.arturbosch.detekt") version "1.8.0"
    id("com.jaredsburrows.spoon")
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        multiDexEnabled = true
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

    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/main.kotlin_module")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/plexus/components.xml")
        exclude("META-INF/sdk_debug.kotlin_module")
        exclude("META-INF/sdk_release.kotlin_module")
        exclude("**/attach_hotspot_windows.dll")
        exclude("META-INF/licenses/**")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }
}

spoon {
    title = "RBS Payment SDK"
    grantAll = true
    clearAppDataBeforeEachTest = true
    noAnimations = true
}

detekt {
    toolVersion = "1.8.0"
    input = files("src/main/kotlin", "src/test/kotlin", "src/androidTest/kotlin")
    parallel = true
    config = files("../config/detekt/detekt.yml")
    reports {
        xml {
            enabled = true
            destination = file("build/reports/detekt.xml")
        }
        html {
            enabled = true
            destination = file("build/reports/detekt.html")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.google.android.material:material:1.2.0-beta01")
    implementation("io.card:android-sdk:5.5.1")
    implementation("com.caverock:androidsvg-aar:1.4")

    testImplementation("junit:junit:4.13")
    testImplementation("io.kotest:kotest-runner-junit4-jvm:4.1.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.1.1")
    testImplementation("androidx.test.ext:junit:1.1.1")
    testImplementation("androidx.test.espresso:espresso-core:3.2.0")
    testImplementation("androidx.test:core:1.2.0")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.7")

    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.7")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.7.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("io.mockk:mockk:1.10.0")
    androidTestImplementation("io.mockk:mockk-android:1.10.0")
    androidTestImplementation("com.squareup.spoon:spoon-client:2.0.0-SNAPSHOT")
}

tasks {
    withType<Test> {
        dependsOn(detekt)
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xuse-experimental=kotlinx.coroutines.ObsoleteCoroutinesApi"
            )
            jvmTarget = "1.8"
        }
    }
}
