package ru.rbs.mobile.payment.sdk.core.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.validation.rules.ExpiryRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.RegExRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringRequiredRule

/**
 * Валидатор значения срока действия карты.
 *
 * @param context контекст для получения строковых ресурсов.
 */
class CardExpiryValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringRequiredRule(
                code = ValidationCodes.required,
                message = context.getString(R.string.rbs_card_incorrect_expiry)
            ),
            RegExRule(
                code = ValidationCodes.invalidFormat,
                message = context.getString(R.string.rbs_card_incorrect_expiry),
                regex = PATTERN
            ),
            ExpiryRule(
                    code = ValidationCodes.invalid,
                    message = context.getString(R.string.rbs_card_incorrect_expiry)
            )
        )
    }

    companion object {
        private val PATTERN = "^\\d{2}/\\d{2}".toRegex()
    }
}
