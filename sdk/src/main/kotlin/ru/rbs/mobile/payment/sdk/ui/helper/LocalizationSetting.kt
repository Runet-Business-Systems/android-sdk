package ru.rbs.mobile.payment.sdk.ui.helper

import java.util.*

internal object LocalizationSetting {

    private var locale: Locale? = null

    @JvmStatic
    fun getDefaultLanguage(): Locale = Locale.getDefault()

    @JvmStatic
    fun setLanguage(locale: Locale) {
        this.locale = locale
    }

    @JvmStatic
    fun getLanguage(): Locale? = locale

    fun getLanguageWithDefault(default: Locale): Locale {
        return getLanguage() ?: run {
            setLanguage(default)
            default
        }
    }
}
