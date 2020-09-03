package ru.rbs.mobile.payment.sdk.component

import ru.rbs.mobile.payment.sdk.model.CardInfo

/**
 * Интерфейс для процессора формирования криптограммы по переданным данным платежа.
 */
interface CryptogramProcessor {

    /**
     * Создает готовую к проведению платежа криптограмму.
     *
     * @param order идентификатор заказа.
     * @param timestamp время платежа.
     * @param uuid уникальный идентификатор.
     * @param cardInfo данные карты, для списания средств.
     * @return криптограмму для переданных данных о платеже.
     */
    suspend fun create(
        order: String,
        timestamp: Long,
        uuid: String,
        cardInfo: CardInfo
    ): String

    /**
     * Создает готовую к проведению платежа криптограмму.
     *
     * @param googlePayToken токен полученный от Google Pay.
     * @return криптограмму для переданных данных о платеже.
     */
    suspend fun create(
        googlePayToken: String
    ): String
}
