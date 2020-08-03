package ru.rbs.mobile.payment.sdk.ui.helper

import android.content.Context
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.utils.digitsOnly

internal object CardNumberFormatter {

    fun maskCardNumber(context: Context, pan: String): String {
        val clearPan = pan.digitsOnly()
        val number = when {
            clearPan.length >= PAN_LAST_DIGITS_COUNT -> clearPan.takeLast(PAN_LAST_DIGITS_COUNT)
            else -> pan
        }
        return context.resources.getString(R.string.rbs_card_list_pan_pattern, number)
    }

    private const val PAN_LAST_DIGITS_COUNT = 4
}
