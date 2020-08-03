package ru.rbs.mobile.payment.sdk.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.validation.rules.LuhnStringRule
import ru.rbs.mobile.payment.sdk.validation.rules.StringLengthRule

/**
 * Валидатор значения номера карты.
 *
 * @param context контекст для получения строковых ресурсов.
 */
class CardNumberValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringLengthRule(
                message = context.getString(R.string.rbs_card_incorrect_length),
                minLength = MIN_LENGTH,
                maxLength = MAX_LENGTH
            ),
            LuhnStringRule(
                message = context.getString(R.string.rbs_card_incorrect_number)
            )
        )
    }

    companion object {
        private const val MIN_LENGTH = 16
        private const val MAX_LENGTH = 19
    }
}
