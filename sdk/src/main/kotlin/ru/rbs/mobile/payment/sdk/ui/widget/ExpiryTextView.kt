package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.layout_bank_card.view.*
import ru.rbs.mobile.payment.sdk.model.ExpiryDate
import ru.rbs.mobile.payment.sdk.utils.digitsOnly

/**
 * UI элемент для вывода срока действия карты.
 */
class ExpiryTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : BaseTextView(context, attrs, defStyleAttr) {

    /**
     * Установка срока действия карты.
     *
     * @param expiry строка срока действия карты в формате "MM/YY".
     */
    fun setExpiry(expiry: String) {
        val digitsOnly = StringBuilder(expiry.digitsOnly(EXPIRY_MASK_LENGTH)).apply {
            if (length >= EXPIRY_MASK_DIVIDER_INDEX) {
                insert(EXPIRY_MASK_DIVIDER_INDEX, EXPIRY_MASK_DIVIDER)
            }
        }
        val formatted = StringBuilder(EXPIRY_MASK)
        formatted.replace(0, digitsOnly.length, digitsOnly.toString())
        cardExpiry.text = formatted
    }

    /**
     * Установка срока действия карты.
     *
     * @param expiry срок действия карты.
     */
    fun setExpiry(expiry: ExpiryDate) =
        setExpiry(StringBuilder().apply {
            append(expiry.expMonth.toString().padStart(2, '0'))
            append((expiry.expYear % EXPIRY_YEAR_DIVIDER).toString().padStart(2, '0'))
        }.toString())

    companion object {
        private const val EXPIRY_MASK_DIVIDER_INDEX = 2
        private const val EXPIRY_MASK_DIVIDER = " / "
        private const val EXPIRY_MASK = "••${EXPIRY_MASK_DIVIDER}••"
        private const val EXPIRY_MASK_LENGTH = 4
        private const val EXPIRY_YEAR_DIVIDER = 100
    }
}
