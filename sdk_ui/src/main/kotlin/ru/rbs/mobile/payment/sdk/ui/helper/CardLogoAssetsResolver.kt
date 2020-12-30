package ru.rbs.mobile.payment.sdk.ui.helper

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.utils.digitsOnly

internal object CardLogoAssetsResolver {

    fun resolveByPan(context: Context, pan: String, preferLight: Boolean = false): String? {
        val cleanPan = pan.digitsOnly()
        val acceptedSystem =
            PAYMENT_SYSTEMS.filter { cleanPan.matches(it.key) }.toList().firstOrNull()
        return if (acceptedSystem != null) {
            resolveByName(context, acceptedSystem.second, preferLight)
        } else {
            null
        }
    }

    fun resolveByName(context: Context, system: String, preferLight: Boolean = false): String? {
        val assetsFolder = "payment_system"
        val iconsList = context.resources.assets.list(assetsFolder)
        return if (preferLight && iconsList?.contains("logo-${system}-white.svg") == true) {
            "${assetsFolder}/logo-${system}-white.svg"
        } else if (iconsList?.contains("logo-${system}.svg") == true) {
            "${assetsFolder}/logo-${system}.svg"
        } else {
            null
        }
    }

    private val PAYMENT_SYSTEMS = mapOf(
        "^3[47]\\d*\$".toRegex() to "amex",
        "^(2131|1800|35)\\d*\$".toRegex() to "jcb",
        "^(5[0678]|6304|6390|6054|6271|67)\\d*\$".toRegex() to "maestro",
        "^(5[1-5]|222[1-9]|2[3-6]|27[0-1]|2720)\\d*\$".toRegex() to "mastercard",
        "^22\\d*\$".toRegex() to "mir",
        "^4\\d*\$".toRegex() to "visa"
    )
}
