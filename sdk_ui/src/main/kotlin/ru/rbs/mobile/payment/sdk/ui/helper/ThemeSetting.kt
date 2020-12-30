package ru.rbs.mobile.payment.sdk.ui.helper

import ru.rbs.mobile.payment.sdk.model.Theme
import ru.rbs.mobile.payment.sdk.utils.defaultTheme

internal object ThemeSetting {

    private var theme: Theme? = null

    @JvmStatic
    fun getDefaultTheme(): Theme = defaultTheme()

    @JvmStatic
    fun setTheme(theme: Theme) {
        this.theme = theme
    }

    @JvmStatic
    fun getTheme(): Theme? = theme

    fun getThemeWithDefault(default: Theme): Theme {
        return getTheme() ?: run {
            setTheme(default)
            default
        }
    }
}
