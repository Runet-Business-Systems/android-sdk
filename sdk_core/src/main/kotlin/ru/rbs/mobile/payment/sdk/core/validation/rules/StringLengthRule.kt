package ru.rbs.mobile.payment.sdk.core.validation.rules

import ru.rbs.mobile.payment.sdk.core.validation.BaseValidationRule
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult.Companion.VALID
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult.Companion.error

/**
 * Правило для проверки строкового значения на диапазон кол-ва символов.
 *
 * @param code код ошибки.
 * @param message сообщение выводимое в случае нахождения значения длины строки за пределами
 * допустимого диапазона.
 * @param minLength минимальная допустимая длина строки.
 * @param maxLength максимальная допустимая длина строки.
 */
class StringLengthRule(
    private val code: String,
    private val message: String,
    private val minLength: Int = 0,
    private val maxLength: Int = Int.MAX_VALUE
) : BaseValidationRule<String> {

    override fun validateForError(data: String): ValidationResult =
        if (data.length < minLength || data.length > maxLength) {
            error(code, message)
        } else {
            VALID
        }
}
