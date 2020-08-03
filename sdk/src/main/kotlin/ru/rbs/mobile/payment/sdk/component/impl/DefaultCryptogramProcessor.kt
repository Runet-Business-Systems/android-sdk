package ru.rbs.mobile.payment.sdk.component.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.rbs.mobile.payment.sdk.component.CryptogramCipher
import ru.rbs.mobile.payment.sdk.component.CryptogramProcessor
import ru.rbs.mobile.payment.sdk.component.KeyProvider
import ru.rbs.mobile.payment.sdk.component.PaymentStringProcessor
import ru.rbs.mobile.payment.sdk.model.CardInfo

/**
 * Реализация процессора для формирования криптограммы.
 *
 * @param keyProvider поставщик ключа шифрования.
 * @param paymentStringProcessor процессор формирования строки оплаты.
 */
class DefaultCryptogramProcessor(
    private val keyProvider: KeyProvider,
    private val paymentStringProcessor: PaymentStringProcessor,
    private val cryptogramCipher: CryptogramCipher
) : CryptogramProcessor {

    override suspend fun create(
        order: String,
        timestamp: Long,
        uuid: String,
        cardInfo: CardInfo
    ): String = withContext(Dispatchers.IO) {
        val key = keyProvider.provideKey()
        val paymentString = paymentStringProcessor.createPaymentString(
            order = order,
            timestamp = timestamp,
            uuid = uuid,
            cardInfo = cardInfo
        )
        cryptogramCipher.encode(paymentString, key)
    }
}
