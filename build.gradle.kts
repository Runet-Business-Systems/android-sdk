// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven(uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/"))
        maven(uri("https://oss.sonatype.org/content/repositories/snapshots"))
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("com.jaredsburrows:gradle-spoon-plugin:1.5.0")
        classpath(kotlin("gradle-plugin", version = "1.3.72"))

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.objenesis:objenesis:2.6")
        }
    }
    repositories {
        google()
        jcenter()
        maven(uri("https://oss.sonatype.org/content/repositories/snapshots"))
        flatDir {
            dirs("libs")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}