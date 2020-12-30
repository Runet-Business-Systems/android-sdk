package ru.rbs.mobile.payment.sdk.core.validation

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.validation.rules.RegExRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringLengthRule
import ru.rbs.mobile.payment.sdk.core.validation.rules.StringRequiredRule

/**
 *  Валидатор для проверки правильности публичного ключа.
 *
 *  @param context контекст приложения.
 * */

class PubKeyValidator(context: Context) : BaseValidator<String>() {

    init {
        addRules(
            StringRequiredRule(
                code = ValidationCodes.required,
                message = context.getString(R.string.rbs_pub_key_required)
            )
        )
    }
}
