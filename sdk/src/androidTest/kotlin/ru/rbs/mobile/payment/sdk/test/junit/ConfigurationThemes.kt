package ru.rbs.mobile.payment.sdk.test.junit

import ru.rbs.mobile.payment.sdk.model.Theme

/**
 * Аннотация для запуска теста с указанным перечислением тем для тестирования.
 *
 * Пример использования:
 *
 *      @Test
 *      @ConfigurationThemes([Theme.DARK, Theme.LIGHT])
 *      fun test() {
 *      }
 *
 * Тест будет запущен два раза, сначала в темной теме "Theme.DARK", затем в светлой "Theme.LIGHT".
 *
 * @param themes массив тем, в к которых должен запуститься тест.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class ConfigurationThemes(val themes: Array<Theme> = [])
