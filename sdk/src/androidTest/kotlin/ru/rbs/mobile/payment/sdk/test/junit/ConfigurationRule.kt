package ru.rbs.mobile.payment.sdk.test.junit

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.rbs.mobile.payment.sdk.model.Theme
import ru.rbs.mobile.payment.sdk.ui.helper.Locales
import ru.rbs.mobile.payment.sdk.ui.helper.LocalizationSetting
import ru.rbs.mobile.payment.sdk.ui.helper.ThemeSetting
import java.util.*

/**
 * JUnit правило для запуска тестов с указанными параметрами окружения.
 *
 * Списки [defaultLocales] и [defaultThemes] используются по умолчанию для всех запускаемых тестов.
 *
 * Если необходимо переопределить параметры индивидуально для теста, можно воспользоваться
 * аннотациями:
 *
 * [ConfigurationLocales] - для переопределения списка локалей.
 * [ConfigurationThemes] - для переопределения списка тем.
 *
 * @param defaultLocales список локалей, в которых должны запускаться тесты по умолчанию.
 * @param defaultThemes список тем, в которых должны запускаться тесты по умолчанию.
 */
class ConfigurationRule(
    private val defaultLocales: List<Locale> = Locales.availableLocales(),
    private val defaultThemes: List<Theme> = listOf(Theme.LIGHT, Theme.DARK)
) : TestRule {

    /**
     * Текущая локаль, в которой происходит выполнение теста.
     */
    lateinit var currentLocale: Locale
        private set

    /**
     * Текущая тема, в которой происходит выполнение теста.
     */
    lateinit var currentTheme: Theme
        private set

    override fun apply(
        base: Statement,
        description: Description
    ): Statement {
        val testLocales = defaultLocales.toMutableList()
        val testThemes = defaultThemes.toMutableList()
        description.annotations.forEach { annotation ->
            when (annotation) {
                is ConfigurationLocales -> {
                    testLocales.clear()
                    testLocales.addAll(annotation.locales.map {
                        Locale.Builder().setLanguage(it).build()
                    })
                }
                is ConfigurationThemes -> {
                    testThemes.clear()
                    testThemes.addAll(annotation.themes)
                }
                is ConfigurationSingle -> {
                    testLocales.clear()
                    testThemes.clear()
                    testLocales.add(Locales.english())
                    testThemes.add(Theme.DARK)
                }
            }
        }
        check(testLocales.isNotEmpty()) { "Test locales list should not be empty" }
        check(testThemes.isNotEmpty()) { "Test themes list should not be empty" }
        return ConfigurationRuleStatement(
            base = base,
            locales = testLocales,
            themes = testThemes
        )
    }

    /**
     * Выполняет запуск указаного [base] выражения теста с перебором по всем возможным комбинациям
     * списка доступных локалей и списка доступных тем для тестирования.
     *
     * @param base выражение теста.
     * @param locales список локалей, в которых будет выполнен запуск теста.
     * @param themes список тем, в которых будет выполнен запуск теста.
     */
    private inner class ConfigurationRuleStatement(
        private val base: Statement,
        private val locales: List<Locale>,
        private val themes: List<Theme>
    ) : Statement() {

        @Throws(Throwable::class)
        override fun evaluate() {
            themes.forEach { theme ->
                currentTheme = theme
                ThemeSetting.setTheme(theme)
                locales.forEach { locale ->
                    LocalizationSetting.setLanguage(locale)
                    currentLocale = locale
                    base.evaluate()
                }
            }
        }
    }
}
