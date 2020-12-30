package ru.rbs.mobile.payment.sdk.core.component

import ru.rbs.mobile.payment.sdk.core.model.CardInfo

/**
 * Интерфейс процессора формирования платежных данных по шаблону.
 */
interface PaymentStringProcessor {

    /**
     * Формирует строку с платежной информацией.
     *
     * @param order идентификатор заказа.
     * @param timestamp время платежа.
     * @param uuid уникальный идентификатор.
     * @param cardInfo данные карты, для списания средств.
     *
     * @return подготовленную строку с информацией о платеже.
     */
    fun createPaymentString(
        order: String,
        timestamp: Long,
        uuid: String,
        cardInfo: CardInfo
    ): String
}
