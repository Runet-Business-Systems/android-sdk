package ru.rbs.mobile.payment.sdk.core.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.validation.rules.RegExRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringLengthRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringRequiredRule

/**
 * Валидатор значения имени владельца карты.
 *
 * @param context контекст для получения строковых ресурсов.
 */
class CardHolderValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringRequiredRule(
                code = ValidationCodes.required,
                message = context.getString(R.string.rbs_card_incorrect_card_holder)
            ),
            StringLengthRule(
                code = ValidationCodes.invalid,
                message = context.getString(R.string.rbs_card_incorrect_card_holder),
                maxLength = MAX_LENGTH
            ),
            RegExRule(
                code = ValidationCodes.invalidFormat,
                message = context.getString(R.string.rbs_card_incorrect_card_holder),
                regex = PATTERN
            )
        )
    }

    companion object {
        private const val MAX_LENGTH = 30
        private val PATTERN = "[a-zA-Z ]+".toRegex()
    }
}
