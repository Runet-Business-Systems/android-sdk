package ru.rbs.mobile.payment.sdk.core.validation

/**
 * Объект содержащий коды ошибок.
 */
object ValidationCodes {
    /**
     * Поле обязательно для заполнения.
     */
    const val required = "required"
    /**
     * Некорректное значение поля.
     */
    const val invalid = "invalid"
    /**
     * Не верный формат данных.
     */
    const val invalidFormat = "invalid-format"
}
