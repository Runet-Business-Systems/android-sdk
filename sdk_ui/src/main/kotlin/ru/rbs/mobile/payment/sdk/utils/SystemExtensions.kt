package ru.rbs.mobile.payment.sdk.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.model.Theme

/**
 * Метод для проверки наличия у устройства камеры.
 *
 * @param context контекст приложения.
 * @return true если на устройстве установлена камера, false в противном случае.
 */
fun deviceHasCamera(context: Context): Boolean =
    context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)

/**
 * Метод для проверки наличия у устройства NFC.
 *
 * @param context контекст приложения.
 * @return true если на устройстве есть NFC, false в противном случае.
 */
fun deviceHasNFC(context: Context): Boolean =
    context.packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)


/**
 * Проверяет доступность Google Play сервисов на устройстве.
 *
 * @param context контекст.
 * @return true если Google Play сервисы доступны, в противном случае false.
 */
fun deviceHasGooglePlayServices(context: Context): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
    return resultCode == ConnectionResult.SUCCESS
}

/**
 * Отображает диалог запроса на включение NFC.
 *
 * @param activity экран на котором надо выполнить запрос включения NFC.
 */
fun askToEnableNfc(activity: Activity) {
    val builder = AlertDialog.Builder(activity, R.style.RBSAlertDialogTheme).apply {
        setTitle(R.string.rbs_nfc_disabled_title)
        setMessage(R.string.rbs_nfc_disabled_message)
        setPositiveButton(R.string.rbs_enable) { _, _ ->
            launchNfcSettings(activity)
        }
        setNegativeButton(R.string.rbs_cancel) { dialog, _ ->
            dialog.dismiss()
        }
    }
    builder.show()
}

/**
 * Запуск окна настроек для управления NFC.
 *
 * @param activity экран на котором надо открыть настройки NFC.
 */
fun launchNfcSettings(activity: Activity) {
    activity.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
}

/**
 * Метод для определения темы по умолчанию.
 *
 * @return тема по умолчанию.
 */
fun defaultTheme(): Theme = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Theme.DEFAULT
    else -> Theme.LIGHT
}

/**
 * Метод установки используемой темы.
 *
 * @param theme тема.
 */
fun AppCompatActivity.setUiTheme(theme: Theme) {
    delegate.localNightMode =
        when (theme) {
            Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DEFAULT -> AppCompatDelegate.getDefaultNightMode()
        }
}
