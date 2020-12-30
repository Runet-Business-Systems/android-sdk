package ru.rbs.mobile.payment.sdk.core.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.validation.rules.RegExRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringLengthRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringRequiredRule

/**
 *  Валидатор значения номера заказа.
 *
 *  @param context контекст для получения строковых ресурсов.
 * */

class OrderNumberValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringRequiredRule(
                code = ValidationCodes.required,
                message = context.getString(R.string.rbs_order_incorrect_length)
            ),
            StringLengthRule(
                code = ValidationCodes.invalid,
                message = context.getString(R.string.rbs_order_incorrect_length),
                minLength = MIN_LENGTH
            ),
            RegExRule(
                code = ValidationCodes.invalid,
                message = context.getString(R.string.rbs_order_incorrect_length),
                regex = PATTERN.toRegex()
            )
        )
    }

    companion object {
        private const val MIN_LENGTH = 1
        private const val PATTERN = "\\S+"
    }
}
