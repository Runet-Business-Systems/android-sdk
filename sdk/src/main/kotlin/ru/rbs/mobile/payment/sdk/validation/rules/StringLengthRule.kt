package ru.rbs.mobile.payment.sdk.validation.rules

import ru.rbs.mobile.payment.sdk.validation.BaseValidationRule

/**
 * Правило для проверки строкового значения на диапазон кол-ва символов.
 *
 * @param message сообщение выводимое в случае нахождения значения длины строки за пределами
 * допустимого диапазона.
 * @param minLength минимальная допустимая длина строки.
 * @param maxLength максимальная допустимая длина строки.
 */
class StringLengthRule(
    private val message: String,
    private val minLength: Int = 0,
    private val maxLength: Int = Int.MAX_VALUE
) : BaseValidationRule<String> {

    override fun validateForError(data: String): String? =
        if (data.length < minLength || data.length > maxLength) {
            message
        } else {
            null
        }
}
