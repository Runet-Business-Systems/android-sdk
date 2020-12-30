package ru.rbs.mobile.payment.sdk.core.validation

/**
 * Описание результата проверки данных.
 *
 * @param isValid true данные корректны, в противном случае false.
 * @param errorCode код ошибки, не меняется при локализации.
 * @param errorMessage сообщение об ошибке в данных, если она выявлена при проверке.
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorCode: String?,
    val errorMessage: String?
) {
    companion object {
        /**
         * @return результат валидации.
         */
        val VALID = ValidationResult(true, null, null)

        /**
         * Метод описывающий ошибку.
         *
         * @param errorCode код ошибки.
         * @param errorMessage сообщение выводимое в случае некорректного значения параметра карты.
         *
         * @return результат валидации.
         * */
        fun error(errorCode: String, errorMessage: String) =
            ValidationResult(false, errorCode, errorMessage)
    }
}
