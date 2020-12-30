package ru.rbs.mobile.payment.sdk.core.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringLengthRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.RegExRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringRequiredRule

/**
 * Валидатор значения секретного кода карты.
 *
 * @param context контекст для получения строковых ресурсов.
 */
class CardCodeValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringRequiredRule(
                code = ValidationCodes.required,
                message = context.getString(R.string.rbs_card_incorrect_cvc)
            ),
            StringLengthRule(
                code = ValidationCodes.invalid,
                message = context.getString(R.string.rbs_card_incorrect_cvc),
                minLength = MIN_LENGTH,
                maxLength = MAX_LENGTH
            ),
            RegExRule(
                code = ValidationCodes.invalid,
                message = context.getString(R.string.rbs_card_incorrect_cvc),
                regex = PATTERN
            )
        )
    }

    companion object {
        private const val MIN_LENGTH = 3
        private const val MAX_LENGTH = 3
        private val PATTERN = "[0-9]+".toRegex()
    }
}
