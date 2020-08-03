package ru.rbs.mobile.payment.sdk.model

/**
 * Возможные состояния формирования данных для выполнения оплаты.
 */
enum class PaymentDataStatus {

    /**
     * Оплата отменена.
     */
    CANCELED,

    /**
     * Данные успешно сформированы.
     */
    SUCCEEDED;

    /**
     * Проверка на соответствие статусу [SUCCEEDED].
     *
     * @return возвращает true если статус [SUCCEEDED], в противном случае false.
     */
    fun isSucceeded() = this == SUCCEEDED

    /**
     * Проверка на соответствие статусу [CANCELED].
     *
     * @return возвращает true если статус [CANCELED], в противном случае false.
     */
    fun isCanceled() = this == CANCELED
}
