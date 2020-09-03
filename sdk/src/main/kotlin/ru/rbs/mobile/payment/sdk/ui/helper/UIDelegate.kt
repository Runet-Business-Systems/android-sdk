package ru.rbs.mobile.payment.sdk.ui.helper


import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.model.Theme
import ru.rbs.mobile.payment.sdk.ui.helper.LocalizationSetting.getDefaultLanguage
import ru.rbs.mobile.payment.sdk.ui.helper.LocalizationSetting.getLanguageWithDefault
import ru.rbs.mobile.payment.sdk.ui.helper.ThemeSetting.getDefaultTheme
import ru.rbs.mobile.payment.sdk.ui.helper.ThemeSetting.getThemeWithDefault
import ru.rbs.mobile.payment.sdk.utils.setUiTheme
import java.util.*

@Suppress("TooManyFunctions")
internal open class UIDelegate(private val activity: AppCompatActivity) {
    private lateinit var currentLanguage: Locale
    private lateinit var currentTheme: Theme

    fun onCreate() {
        setup(true)
    }

    fun onResume() {
        Handler().post {
            checkSettingsChanged(false)
        }
    }

    fun attachBaseContext(context: Context): Context {
        val locale = getLanguageWithDefault(
            getDefaultLanguage()
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
        val currentLocale = getLanguageWithDefault(
            getDefaultLanguage()
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

    private fun setup(isCreate: Boolean) {
        LocalizationSetting.getLanguage()?.let { locale -> currentLanguage = locale }
        ThemeSetting.getTheme()?.let { theme -> currentTheme = theme }
        checkSettingsChanged(isCreate)
    }

    private fun isCurrentLanguageSetting(newLocale: Locale, currentLocale: Locale): Boolean {
        return newLocale.toString() == currentLocale.toString()
    }

    private fun isCurrentThemeSetting(newTheme: Theme, currentTheme: Theme): Boolean {
        return newTheme == currentTheme
    }

    private fun checkSettingsChanged(isCreate: Boolean) {
        val oldLanguage = getLanguageWithDefault(getDefaultLanguage())
        val oldTheme = getThemeWithDefault(getDefaultTheme())
        val localeChanged = !isCurrentLanguageSetting(currentLanguage, oldLanguage)
        val themeChanged = !isCurrentThemeSetting(currentTheme, oldTheme)
        if (localeChanged || themeChanged || isCreate) {
            if (themeChanged || isCreate) {
                activity.window.setWindowAnimations(R.style.RBSWindowAnimationFadeInOut)
                activity.setUiTheme(currentTheme)
            } else if (localeChanged && !(themeChanged || isCreate)) {
                activity.recreate()
            }
        }
    }
}
