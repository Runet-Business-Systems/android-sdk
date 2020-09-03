@file:Suppress("UndocumentedPublicClass")

object Versions {
    const val kotlin_stdlib = "1.3.72"
    const val kotlinx_coroutines_core = "1.3.7"
    const val kotlinx_coroutines_android = "1.3.7"
    const val appcompat = "1.1.0"
    const val androidx_constraintlayout = "1.1.3"
    const val android_material = "1.2.0-beta01"
    const val io_card_android_sdk = "5.5.1"
    const val com_caverock_androidsvg = "1.4"
    const val com_github_devnied_emvnfccard = "3.0.1"
    const val android_play_services_wallet = "18.0.0"

    const val junit = "4.13"
    const val io_kotest_runner_junit = "4.1.1"
    const val io_kotest_assertion_core = "4.1.1"
    const val androidx_test_ext_junit = "1.1.1"
    const val androidx_test_rules = "1.2.0"
    const val androidx_test_espresso_core = "3.2.0"
    const val androidx_test_core = "1.2.0"
    const val io_mockk = "1.10.0"
    const val kotlinx_coroutines_test = "1.3.7"
    const val com_squareup_spoon = "2.0.0-SNAPSHOT"
    const val com_squareup_mockwebserver = "4.7.2"
}

object Libs {
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_stdlib}"
    const val kotlinx_coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx_coroutines_core}"
    const val kotlinx_coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinx_coroutines_android}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val androidx_constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.androidx_constraintlayout}"
    const val android_material = "com.google.android.material:material:${Versions.android_material}"
    const val io_card_android_sdk = "io.card:android-sdk:${Versions.io_card_android_sdk}"
    const val com_caverock_androidsvg = "com.caverock:androidsvg-aar:${Versions.com_caverock_androidsvg}"
    const val com_github_devnied_emvnfccard =
        "com.github.devnied.emvnfccard:library:${Versions.com_github_devnied_emvnfccard}"
    const val android_play_services_wallet =
        "com.google.android.gms:play-services-wallet:${Versions.android_play_services_wallet}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
    const val io_kotest_runner_junit =
        "io.kotest:kotest-runner-junit4-jvm:${Versions.io_kotest_runner_junit}"
    const val io_kotest_assertion_core =
        "io.kotest:kotest-assertions-core-jvm:${Versions.io_kotest_assertion_core}"
    const val androidx_test_ext_junit = "androidx.test.ext:junit:${Versions.androidx_test_ext_junit}"
    const val androidx_test_espresso_core =
        "androidx.test.espresso:espresso-core:${Versions.androidx_test_espresso_core}"
    const val androidx_test_core = "androidx.test:core:${Versions.androidx_test_core}"
    const val kotlinx_coroutines_test =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinx_coroutines_test}"
    const val androidx_test_rules = "androidx.test:rules:${Versions.androidx_test_rules}"
    const val io_mockk = "io.mockk:mockk:${Versions.io_mockk}"
    const val io_mockk_android = "io.mockk:mockk-android:${Versions.io_mockk}"
    const val com_squareup_spoon = "com.squareup.spoon:spoon-client:${Versions.com_squareup_spoon}"
    const val com_squareup_mockwebserver = "com.squareup.okhttp3:mockwebserver:${Versions.com_squareup_mockwebserver}"
}
