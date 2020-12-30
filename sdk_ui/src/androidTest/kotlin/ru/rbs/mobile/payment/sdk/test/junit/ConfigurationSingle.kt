package ru.rbs.mobile.payment.sdk.test.junit

/**
 * Аннотация для запуска теста в одной локали и в одной теме.
 *
 * Используется для запуска тестов, для которых нет необходимости выполнять перебор тем и локалей.
 *
 * Пример использования:
 *
 *      @Test
 *      @ConfigurationSingle
 *      fun test() {
 *      }
 *
 * Тест будет запущен в локали "en" светлой теме "Theme.LIGHT".
 */
@Target(AnnotationTarget.FUNCTION)
annotation class ConfigurationSingle
