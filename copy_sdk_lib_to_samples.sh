#!/bin/bash
set -e

./gradlew :sdk_core:build
./gradlew :sdk_ui:build

mkdir -p app_kotlin_core/libs/ && cp sdk_core/build/outputs/aar/sdk_core-release.aar app_kotlin_core/libs/
mkdir -p app_kotlin_ui/libs/ && cp sdk_core/build/outputs/aar/sdk_core-release.aar app_kotlin_ui/libs/
mkdir -p app_java_ui/libs/ && cp sdk_core/build/outputs/aar/sdk_core-release.aar app_java_ui/libs/

mkdir -p app_kotlin_ui/libs/ && cp sdk_ui/build/outputs/aar/sdk_ui-release.aar app_kotlin_ui/libs/
mkdir -p app_java_ui/libs/ && cp sdk_ui/build/outputs/aar/sdk_ui-release.aar app_java_ui/libs/

mkdir -p app_kotlin_ui/libs/ && cp sdk_three_ds/sdk_threeds-release.aar app_kotlin_ui/libs/
mkdir -p app_kotlin_ui/libs/ && cp sdk_three_ds/sdk_listeners_android.jar app_kotlin_ui/libs/