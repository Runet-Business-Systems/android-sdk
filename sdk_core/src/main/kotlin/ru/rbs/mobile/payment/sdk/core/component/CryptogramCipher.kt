package ru.rbs.mobile.payment.sdk.core.component

import ru.rbs.mobile.payment.sdk.core.model.Key

/**
 * Интерфейс для шифрования криптограммы.
 */
interface CryptogramCipher {

    /**
     * Шифрует [data] при помощи публичного ключа [key].
     *
     * @return криптограмму.
     */
    fun encode(data: String, key: Key): String
}
