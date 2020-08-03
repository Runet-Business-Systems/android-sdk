package ru.rbs.mobile.payment.sdk.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.validation.rules.RegExRule
import ru.rbs.mobile.payment.sdk.validation.rules.StringLengthRule

/**
 * Валидатор значения имени владельца карты.
 *
 * @param context контекст для получения строковых ресурсов.
 */
class CardHolderValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringLengthRule(
                message = context.getString(R.string.rbs_card_incorrect_card_holder),
                maxLength = MAX_LENGTH
            ),
            RegExRule(
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
