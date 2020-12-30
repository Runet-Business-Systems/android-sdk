package ru.rbs.mobile.payment.sdk.core.validation.rules

import ru.rbs.mobile.payment.sdk.core.validation.BaseValidationRule
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult.Companion.VALID

/**
 * Правило для проверки числового значения в указанном диапазоне.
 *
 * @param code код ошибки.
 * @param message сообщение выводимое в случае нахождения числового значения за пределами допустимого
 * диапазона.
 * @param min минимальное допустимое значение.
 * @param max максимально допустимое значение.
 */
class IntRangeRule(
    private val code: String,
    private val message: String,
    private val min: Int = 0,
    private val max: Int = Int.MAX_VALUE
) : BaseValidationRule<Int> {
    override fun validateForError(data: Int): ValidationResult {
        return if (data < min || data > max) {
            ValidationResult.error(code, message)
        } else {
            VALID
        }
    }
}
