package ru.rbs.mobile.payment.sdk.component

import ru.rbs.mobile.payment.sdk.model.Key

/**
 * Интерфейс для шифрования криптограммы.
 */
interface CryptogramCipher {

    /**
     * Шифрует [data] при помощи публичного ключа [key].
     *
     * @return криптограмму.
     */
    suspend fun encode(data: String, key: Key): String
}
