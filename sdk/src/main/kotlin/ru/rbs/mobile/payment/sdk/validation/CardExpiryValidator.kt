package ru.rbs.mobile.payment.sdk.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.validation.rules.ExpiryRule
import ru.rbs.mobile.payment.sdk.validation.rules.RegExRule

/**
 * Валидатор значения срока действия карты.
 *
 * @param context контекст для получения строковых ресурсов.
 */
class CardExpiryValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            RegExRule(
                message = context.getString(R.string.rbs_card_incorrect_expiry),
                regex = PATTERN
            ),
            ExpiryRule(
                message = context.getString(R.string.rbs_card_incorrect_expiry)
            )
        )
    }

    companion object {
        private val PATTERN = "^\\d{2}/\\d{2}".toRegex()
    }
}
