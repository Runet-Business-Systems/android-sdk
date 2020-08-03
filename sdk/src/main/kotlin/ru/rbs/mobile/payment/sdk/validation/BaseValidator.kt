package ru.rbs.mobile.payment.sdk.validation

/**
 * Базовый класс для создания валидаторов данных.
 */
open class BaseValidator<DATA> {

    private val rules = mutableListOf<BaseValidationRule<DATA>>()

    /**
     * Добавление правил проверки в валидатор.
     */
    fun addRules(vararg checker: BaseValidationRule<DATA>) {
        rules.addAll(checker)
    }

    /**
     * Проверка данных [data] по списку предопределенных правил проверки.
     *
     * @param data данные для проверки.
     */
    fun validate(data: DATA): ValidationResult {
        rules.forEach { checker ->
            val error = checker.validateForError(data)
            if (error != null) {
                return ValidationResult(false, error)
            }
        }
        return ValidationResult(true)
    }

    /**
     * Описание результата проверки данных.
     *
     * @param isValid true данные корректны, в противном случае false.
     * @param errorMessage сообщение об ошибке в данных, если она выявлена при проверке.
     */
    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )
}
