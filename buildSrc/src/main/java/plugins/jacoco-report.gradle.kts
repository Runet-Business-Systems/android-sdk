package plugins

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
    }
}

val classDirectoriesTree = fileTree(buildDir) {
    include(
        "**/classes/**/main/**",
        "**/intermediates/classes/debug/**",
        "**/intermediates/javac/debug/*/classes/**",
        "**/tmp/kotlin-classes/debug/**"
    )

    exclude(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*\$Lambda$*.*",
        "**/*\$inlined$*.*"
    )
}

val sourceDirectoriesTree = fileTree(buildDir) {
    include(
        "src/main/java/**",
        "src/main/kotlin/**",
        "src/debug/java/**",
        "src/debug/kotlin/**"
    )
}

val executionDataTree = fileTree(buildDir) {
    include(
        "outputs/code_coverage/**/*.ec",
        "jacoco/jacocoTestReportDebug.exec",
        "jacoco/testDebugUnitTest.exec",
        "jacoco/test.exec"
    )
}

if (tasks.findByName("jacocoAndroidTestReport") == null) {

    tasks.register<JacocoReport>("jacocoAndroidTestReport") {
        group = "verification"
        description = "Code coverage report for both Android and Unit tests."
        dependsOn("testDebugUnitTest", "createDebugCoverageReport")
        reports {
            xml.isEnabled = true
            html.isEnabled = true
            xml.destination =
                file("${buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
            html.destination = file("${buildDir}/reports/jacoco/jacocoTestReport/html")
        }
        sourceDirectories.setFrom(sourceDirectoriesTree)
        classDirectories.setFrom(classDirectoriesTree)
        executionData.setFrom(executionDataTree)
    }
}
