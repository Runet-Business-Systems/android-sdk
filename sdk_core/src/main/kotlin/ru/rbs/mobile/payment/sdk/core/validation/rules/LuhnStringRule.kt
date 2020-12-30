package ru.rbs.mobile.payment.sdk.core.validation.rules

import ru.rbs.mobile.payment.sdk.core.validation.BaseValidationRule
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult.Companion.VALID
import ru.rbs.mobile.payment.sdk.core.validation.ValidationResult.Companion.error

/**
 * Правило для проверки строкового значения номера карты на соответствие алгоритму Luhn.
 *
 * @param code код ошибки.
 * @param message сообщение выводимое в случае ошибки после проверки по алгоритму Luhn.
 */
class LuhnStringRule(
    private val code: String,
    private val message: String
) :
    BaseValidationRule<String> {

    @Suppress("MagicNumber")
    override fun validateForError(data: String): ValidationResult {
        val isValid = data.reversed()
            .map(Character::getNumericValue)
            .mapIndexed { index, digit ->
                when {
                    index % 2 == 0 -> digit
                    digit < 5 -> digit * 2
                    else -> digit * 2 - 9
                }
            }.sum() % 10 == 0
        return if (!isValid) error(code, message) else VALID
    }
}
