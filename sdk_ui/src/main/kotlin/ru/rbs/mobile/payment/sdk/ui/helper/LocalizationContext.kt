package ru.rbs.mobile.payment.sdk.ui.helper

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics

internal class LocalizationContext(base: Context) : ContextWrapper(base) {
    override fun getResources(): Resources {
        val locale = LocalizationSetting.getLanguageWithDefault(LocalizationSetting.getDefaultLanguage())
        val configuration = super.getResources().configuration

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> configuration.setLocales(LocaleList(locale))
            else -> {
                @Suppress("DEPRECATION")
                configuration.locale = locale
            }
        }
        val metrics: DisplayMetrics = super.getResources().displayMetrics
        @Suppress("DEPRECATION")
        return Resources(assets, metrics, configuration)
    }
}
