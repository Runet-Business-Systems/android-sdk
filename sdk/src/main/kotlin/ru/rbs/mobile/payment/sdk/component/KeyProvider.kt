package ru.rbs.mobile.payment.sdk.component

import ru.rbs.mobile.payment.sdk.model.Key

/**
 * Интерфейс провайдера ключа для шифрования платежной информации.
 */
interface KeyProvider {

    /**
     * Возвращает активный ключа для шифрования платежной информации.
     *
     * @return активный ключ.
     */
    @Throws(KeyProviderException::class)
    suspend fun provideKey(): Key
}
