package ru.rbs.mobile.payment.sdk.component.impl

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.rbs.mobile.payment.sdk.core.component.CryptogramCipher
import ru.rbs.mobile.payment.sdk.core.component.PaymentStringProcessor
import ru.rbs.mobile.payment.sdk.core.model.CardInfo

/**
 * Реализация процессора для формирования криптограммы.
 *
 * @param keyProvider поставщик ключа шифрования.
 * @param paymentStringProcessor процессор формирования строки оплаты.
 */
class DefaultCryptogramProcessor(
    private val keyProvider: ru.rbs.mobile.payment.sdk.component.KeyProvider,
    private val paymentStringProcessor: PaymentStringProcessor,
    private val cryptogramCipher: CryptogramCipher
) : ru.rbs.mobile.payment.sdk.component.CryptogramProcessor {

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

    override suspend fun create(googlePayToken: String): String = withContext(Dispatchers.IO) {
        Base64.encodeToString(googlePayToken.toByteArray(), Base64.NO_WRAP)
    }
}
