import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("com.jaredsburrows.spoon")
    id("jacoco")
    id("plugins.jacoco-report")
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        multiDexEnabled = true
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isTestCoverageEnabled = false
        }
        getByName("debug") {
            isTestCoverageEnabled = true
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
            isReturnDefaultValues = true
        }
    }
}

spoon {
    title = "RBS Payment SDK"
    grantAll = true
    clearAppDataBeforeEachTest = true
    noAnimations = true
}

jacoco {
    toolVersion = "0.8.4"
}

dependencies {
    implementation(Libs.kotlin_stdlib)
    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)
    implementation(Libs.appcompat)
    implementation(Libs.androidx_constraintlayout)
    implementation(Libs.android_material)
    implementation(Libs.io_card_android_sdk)
    implementation(Libs.com_caverock_androidsvg)
    implementation(Libs.com_github_devnied_emvnfccard)
    implementation(Libs.android_play_services_wallet)

    testImplementation(TestLibs.junit)
    testImplementation(TestLibs.io_kotest_runner_junit)
    testImplementation(TestLibs.io_kotest_assertion_core)
    testImplementation(TestLibs.androidx_test_ext_junit)
    testImplementation(TestLibs.androidx_test_core)
    testImplementation(TestLibs.io_mockk)
    testImplementation(TestLibs.kotlinx_coroutines_test)

    androidTestImplementation(TestLibs.kotlinx_coroutines_test)
    androidTestImplementation(TestLibs.androidx_test_ext_junit)
    androidTestImplementation(TestLibs.androidx_test_rules)
    androidTestImplementation(TestLibs.androidx_test_espresso_core)
    androidTestImplementation(TestLibs.io_mockk_android)
    androidTestImplementation(TestLibs.com_squareup_spoon)
    androidTestImplementation(TestLibs.com_squareup_mockwebserver)
}

tasks {
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
