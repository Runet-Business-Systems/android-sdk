#!/bin/bash

./gradlew :sdk:build
mkdir -p cp app_kotlin/libs/ && cp sdk/build/outputs/aar/sdk-release.aar app_kotlin/libs/
mkdir -p cp app_java/libs/ && cp sdk/build/outputs/aar/sdk-release.aar app_java/libs/