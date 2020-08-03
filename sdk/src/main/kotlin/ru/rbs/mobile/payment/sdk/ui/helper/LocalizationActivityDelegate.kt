package ru.rbs.mobile.payment.sdk.ui.helper


import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.os.LocaleList
import java.util.*

@Suppress("TooManyFunctions")
internal open class LocalizationActivityDelegate(private val activity: Activity) {
    private var isLocalizationChanged = false
    private lateinit var currentLanguage: Locale

    companion object {
        private const val KEY_ACTIVITY_LOCALE_CHANGED = "activity_locale_changed"
    }

    fun onCreate() {
        setupLanguage()
        checkBeforeLocaleChanging()
    }

    fun onResume() {
        Handler().post {
            checkLocaleChange()
            checkAfterLocaleChanging()
        }
    }

    fun attachBaseContext(context: Context): Context {
        val locale = LocalizationSetting.getLanguageWithDefault(
            LocalizationSetting.getDefaultLanguage()
        )
        val config = context.resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            config.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
            context.createConfigurationContext(config)
        } else context
    }

    fun getApplicationContext(applicationContext: Context): Context {
        val baseLocale = getLocaleFromConfiguration(applicationContext.resources.configuration)
        val currentLocale = LocalizationSetting.getLanguageWithDefault(
            LocalizationSetting.getDefaultLanguage()
        )
        return if (!baseLocale.toString().equals(currentLocale.toString(), ignoreCase = true)) {
            val context = LocalizationContext(applicationContext)
            val config = context.resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                config.setLocale(currentLocale)
                val localeList = LocaleList(currentLocale)
                LocaleList.setDefault(localeList)
                config.setLocales(localeList)
                context.createConfigurationContext(config)
            } else {
                @Suppress("DEPRECATION")
                config.locale = currentLocale
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(
                    config,
                    context.resources.displayMetrics
                )
                context
            }
        } else {
            applicationContext
        }
    }

    @Suppress("DEPRECATION")
    fun getLocaleFromConfiguration(configuration: Configuration): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            configuration.locales.get(0)
        } else {
            configuration.locale
        }
    }

    @Suppress("DEPRECATION")
    fun getResources(resources: Resources): Resources {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val locale = LocalizationSetting.getLanguage()
            val config = resources.configuration
            config.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
            resources
        } else {
            val config = resources.configuration
            config.locale = LocalizationSetting.getLanguage()
            val metrics = resources.displayMetrics
            Resources(activity.assets, metrics, config)
        }
    }

    private fun checkBeforeLocaleChanging() {
        val isLocalizationChanged =
            activity.intent.getBooleanExtra(KEY_ACTIVITY_LOCALE_CHANGED, false)
        if (isLocalizationChanged) {
            this.isLocalizationChanged = true
            activity.intent.removeExtra(KEY_ACTIVITY_LOCALE_CHANGED)
        }
    }

    private fun setupLanguage() {
        LocalizationSetting.getLanguage()?.let { locale ->
            currentLanguage = locale
        } ?: run {
            checkLocaleChange()
        }
    }

    private fun isCurrentLanguageSetting(newLocale: Locale, currentLocale: Locale): Boolean {
        return newLocale.toString() == currentLocale.toString()
    }

    private fun notifyLanguageChanged() {
        activity.intent.putExtra(KEY_ACTIVITY_LOCALE_CHANGED, true)
        activity.recreate()
    }

    private fun checkLocaleChange() {
        val oldLanguage = LocalizationSetting.getLanguageWithDefault(
            LocalizationSetting.getDefaultLanguage()
        )
        if (!isCurrentLanguageSetting(currentLanguage, oldLanguage)) {
            isLocalizationChanged = true
            notifyLanguageChanged()
        }
    }

    private fun checkAfterLocaleChanging() {
        if (isLocalizationChanged) {
            isLocalizationChanged = false
        }
    }
}
