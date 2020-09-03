package ru.rbs.mobile.payment.sdk.test.junit

/**
 * Аннотация для запуска теста с указанным перечислением локалей для тестирования.
 *
 * Пример использования:
 *
 *      @Test
 *      @ConfigurationLocales(["fr", "es"])
 *      fun test() {
 *      }
 *
 * Тест будет запущен два раза, сначала в локали "fr", затем  в "es".
 *
 * @param locales массив локалей, в к которых должен запуститься тест.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class ConfigurationLocales(val locales: Array<String> = [])
