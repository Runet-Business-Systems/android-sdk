package ru.rbs.mobile.payment.sdk.validation.rules

import ru.rbs.mobile.payment.sdk.validation.BaseValidationRule

/**
 * Правило для проверки строкового значения по регулярному выражению.
 *
 * @param message сообщение выводимое в случае несоответствия регулярному выражению.
 * @param regex регулярное выражение для проверки строки.
 */
class RegExRule(
    private val message: String,
    private val regex: Regex
) : BaseValidationRule<String> {

    override fun validateForError(data: String): String? = if (!data.matches(regex)) {
        message
    } else {
        null
    }
}
