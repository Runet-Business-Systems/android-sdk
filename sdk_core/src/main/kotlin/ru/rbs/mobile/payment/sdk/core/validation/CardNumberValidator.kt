package ru.rbs.mobile.payment.sdk.core.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.validation.rules.LuhnStringRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.RegExRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringLengthRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringRequiredRule

/**
 * Валидатор значения номера карты.
 *
 * @param context контекст для получения строковых ресурсов.
 */
class CardNumberValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringRequiredRule(
                code = ValidationCodes.required,
                message = context.getString(R.string.rbs_card_incorrect_number)
            ),
            RegExRule(
                code = ValidationCodes.invalidFormat,
                message = context.getString(R.string.rbs_card_incorrect_number),
                regex = PATTERN
            ),
            StringLengthRule(
                code = ValidationCodes.invalid,
                message = context.getString(R.string.rbs_card_incorrect_length),
                minLength = MIN_LENGTH,
                maxLength = MAX_LENGTH
            ),
            LuhnStringRule(
                code = ValidationCodes.invalid,
                message = context.getString(R.string.rbs_card_incorrect_number)
            )
        )
    }

    companion object {
        private const val MIN_LENGTH = 16
        private const val MAX_LENGTH = 19
        private val PATTERN = "[0-9]+".toRegex()
    }
}
