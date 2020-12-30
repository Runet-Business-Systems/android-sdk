package ru.rbs.mobile.payment.sdk.core.validation

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
            val result = checker.validateForError(data)
            if (!result.isValid) {
                return result
            }
        }
        return ValidationResult(true, null, null)
    }
}
