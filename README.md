# rbs-android-sdk

Android library for integrating payments into a mobile app.

## Project structure

* sdk_core - the source code of the base part of the library.
* sdk_ui - the source code of the UI part of the library.
* documentation - documentation on integrating the sdk into applications.
* app_kotlin_core - an example of using the library in a kotlin application without UI.
* app_kotlin_ui - an example of using the library in a kotlin application with UI.
* app_java_ui - an example of using the library in a java application with UI.
* config - code validation configurations.


## SDK integration into the application examples

It uses the library copying , instead of setting up a dependency on the sdk subproject, to
reproduce all the integration steps of the sdk application as in the end user.

Before building examples, you must run the command

```shell script
./copy_sdk_lib_to_samples.sh
```

The script will build and add the library files to the examples (in the libs folder).

## Using the SDK with ProGuard

When using the SDK, you need to migrate the rules files to the build.gradle project config file.
The rules files are located in the project root folder (proguard-sdk-three-ds.pro, proguard-sdk-ui.pro).