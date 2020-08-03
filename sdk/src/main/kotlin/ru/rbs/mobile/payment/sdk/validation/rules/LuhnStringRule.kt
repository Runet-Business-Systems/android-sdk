package ru.rbs.mobile.payment.sdk.validation.rules

import ru.rbs.mobile.payment.sdk.validation.BaseValidationRule

/**
 * Правило для проверки строкового значения номера карты на соответствие алгоритму Luhn.
 *
 * @param message сообщение выводимое в случае ошибки после проверки по алгоритму Luhn.
 */
class LuhnStringRule(private val message: String) : BaseValidationRule<String> {

    @Suppress("MagicNumber")
    override fun validateForError(data: String): String? {
        val isValid = data.reversed()
            .map(Character::getNumericValue)
            .mapIndexed { index, digit ->
                when {
                    index % 2 == 0 -> digit
                    digit < 5 -> digit * 2
                    else -> digit * 2 - 9
                }
            }.sum() % 10 == 0
        return if (!isValid) message else null
    }
}
