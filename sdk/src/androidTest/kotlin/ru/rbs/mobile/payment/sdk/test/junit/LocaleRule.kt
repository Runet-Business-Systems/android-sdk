package ru.rbs.mobile.payment.sdk.test.junit

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import ru.rbs.mobile.payment.sdk.ui.helper.LocalizationSetting
import java.util.*

class LocaleRule(private val locales: List<Locale>) : TestRule {

    override fun apply(
        base: Statement,
        description: Description
    ): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                locales.forEach {
                    LocalizationSetting.setLanguage(
                        locale = it
                    )
                    base.evaluate()
                }
            }
        }
    }
}
