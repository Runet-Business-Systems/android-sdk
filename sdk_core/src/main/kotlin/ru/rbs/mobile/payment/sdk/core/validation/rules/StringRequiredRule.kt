package ru.rbs.mobile.payment.sdk.core.validation.rules

import ru.rbs.mobile.payment.sdk.core.validation.BaseValidationRule
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult

/**
 * Правило для проверки поля на пустоту.
 *
 * @param code код ошибки.
 * @param message сообщение выводимое в случае нахождения числового значения за пределами допустимого
 * диапазона.
 */

class StringRequiredRule(
    private val code: String,
    private val message: String
) : BaseValidationRule<String> {

    override fun validateForError(data: String): ValidationResult {
        return if (data.isBlank()) {
            ValidationResult.error(code, message)
        } else {
            ValidationResult.VALID
        }
    }
}
