package ru.rbs.mobile.payment.sdk.validation

/**
 * Базовый класс для создания правил проверки данных.
 */
interface BaseValidationRule<DATA> {

    /**
     * Метод вызывается при проверке данных [data].
     *
     * @param data данные для проверки.
     * @return null если данные соответствуют правилу, в противном случае текст ошибки.
     */
    fun validateForError(data: DATA): String?
}
