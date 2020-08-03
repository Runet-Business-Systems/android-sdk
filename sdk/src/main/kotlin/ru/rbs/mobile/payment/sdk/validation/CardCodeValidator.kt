package ru.rbs.mobile.payment.sdk.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.validation.rules.StringLengthRule

/**
 * Валидатор значения секретного кода карты.
 *
 * @param context контекст для получения строковых ресурсов.
 */
class CardCodeValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringLengthRule(
                message = context.getString(R.string.rbs_card_incorrect_cvc),
                minLength = MIN_LENGTH,
                maxLength = MAX_LENGTH
            )
        )
    }

    companion object {
        private const val MIN_LENGTH = 3
        private const val MAX_LENGTH = 3
    }
}
