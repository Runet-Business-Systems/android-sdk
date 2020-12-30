package ru.rbs.mobile.payment.sdk.core.validation

/**
 * Базовый класс для создания правил проверки данных.
 */
interface BaseValidationRule<DATA> {

    /**
     * Метод вызывается при проверке данных [data].
     *
     * @param data данные для проверки.
     * @return null если данные соответствуют правилу, в противном случае текст пару со значением
     * кода ошибки и текста ошибки.
     */
    fun validateForError(data: DATA): ValidationResult
}
