package ru.rbs.mobile.payment.sdk.validation.rules

import ru.rbs.mobile.payment.sdk.validation.BaseValidationRule

/**
 * Правило для проверки числового значения в указанном диапазоне.
 *
 * @param message сообщение выводимое в случае нахождения числового значения за пределами допустимого
 * диапазона.
 * @param min минимальное допустимое значение.
 * @param max максимально допустимое значение.
 */
class IntRangeRule(
    private val message: String,
    private val min: Int = 0,
    private val max: Int = Int.MAX_VALUE
) : BaseValidationRule<Int> {

    override fun validateForError(data: Int): String? =
        if (data < min || data > max) {
            message
        } else {
            null
        }
}
