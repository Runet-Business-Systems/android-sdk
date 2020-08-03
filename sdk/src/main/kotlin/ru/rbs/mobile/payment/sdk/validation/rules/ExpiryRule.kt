package ru.rbs.mobile.payment.sdk.validation.rules

import ru.rbs.mobile.payment.sdk.utils.digitsOnly
import ru.rbs.mobile.payment.sdk.validation.BaseValidationRule
import java.util.*

/**
 * Правило для проверки корректности формата срока действия карты.
 *
 * @param message сообщение выводимое в случае некорректного значения срока действия карты.
 */
class ExpiryRule(private val message: String) :
    BaseValidationRule<String> {
    private val monthChecker =
        IntRangeRule(
            message = message,
            min = MONTH_MIN,
            max = MONTH_MAX
        )

    private val yearChecker =
        IntRangeRule(
            message = message,
            min = YEAR_MIN,
            max = YEAR_MAX
        )

    override fun validateForError(data: String): String? {
        val digits = data.digitsOnly()
        val month = digits.take(2).toIntOrNull() ?: INVALID_FIELD_VALUE
        val year = digits.takeLast(2).toIntOrNull() ?: INVALID_FIELD_VALUE
        return monthChecker.validateForError(month) ?: yearChecker.validateForError(year)
    }

    companion object {
        private const val MONTH_MIN = 1
        private const val MONTH_MAX = 12
        private const val INVALID_FIELD_VALUE = -1
        private const val MAX_YEARS = 10
        private val YEAR_MIN = Calendar.getInstance().get(Calendar.YEAR) % 100
        private val YEAR_MAX = (Calendar.getInstance().get(Calendar.YEAR) + MAX_YEARS) % 100
    }
}
