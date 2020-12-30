package ru.rbs.mobile.payment.sdk.component

/**
 * Интерфейс для получения информации о карте по ее номеру.
 */
interface CardInfoProvider {

    /**
     * Метод получения информации о карте по ее номеру.
     *
     * @param bin номер карты или первые 6-8 цифр номера.
     */
    suspend fun resolve(bin: String): CardInfo
}
