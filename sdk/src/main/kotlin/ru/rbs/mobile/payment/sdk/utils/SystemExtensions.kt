package ru.rbs.mobile.payment.sdk.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * Метод для проверки наличия у устройства камеры.
 *
 * @param context контекст приложения.
 * @return true если на устройстве установлена камера, false в противном случае.
 */
fun deviceHasCamera(context: Context): Boolean =
    context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
