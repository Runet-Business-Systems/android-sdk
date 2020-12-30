package ru.rbs.mobile.payment.sdk.core.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.validation.rules.RegExRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringRequiredRule

/**
 *  Валидатор значения идентификатора привязки.
 *
 *  @param context контекст для получения строковых ресурсов.
 * */

class CardBindingIdValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringRequiredRule(
                code = ValidationCodes.required,
                message = context.getString(R.string.rbs_binding_required)
            ),
            RegExRule(
                code = ValidationCodes.invalid,
                message = context.getString(R.string.rbs_binding_incorrect),
                regex = PATTERN.toRegex()
            )
        )
    }

    companion object {
        private const val PATTERN = "\\S+"
    }
}
