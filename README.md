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

# rbs-android-sdk

Android библиотека для интеграции платежей в мобильное приложение.

## Структура проекта

* sdk_core - исходный код базовой части библиотеки.
* sdk_ui - исходный код UI части библиотеки.
* documentation - документация по интеграции sdk в приложения.
* app_kotlin_core - пример использования библиотеки в приложении на языке kotlin без UI.
* app_kotlin_ui - пример использования библиотеки в приложении на языке kotlin с UI.
* app_java_ui - пример использования библиотеки в приложении на языке java с UI.
* config - конфигурации проверки кода.


## Подключение SDK в приложения примеры

Используется копирование библиотеки, вместо настройки зависимости от подпроекта sdk, чтобы 
воспроизвести все шаги интеграции sdk приложение как у конечного пользователя.

Перед сборкой примеров необходимо выполнить команду

```shell script
./copy_sdk_lib_to_samples.sh
```

Скрипт соберет и добавить файлы библиотеки в примеры (в папку libs).