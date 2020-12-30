package ru.rbs.mobile.payment.sdk.core.validation.rules

import ru.rbs.mobile.payment.sdk.core.validation.BaseValidationRule
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult.Companion.VALID
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult.Companion.error

/**
 * Правило для проверки строкового значения по регулярному выражению.
 *
 * @param code код ошибки.
 * @param message сообщение выводимое в случае несоответствия регулярному выражению.
 * @param regex регулярное выражение для проверки строки.
 */
class RegExRule(
    private val code: String,
    private val message: String,
    private val regex: Regex
) : BaseValidationRule<String> {
    override fun validateForError(data: String): ValidationResult = if (!data.matches(regex)) {
        error(code, message)
    } else {
        VALID
    }
}
