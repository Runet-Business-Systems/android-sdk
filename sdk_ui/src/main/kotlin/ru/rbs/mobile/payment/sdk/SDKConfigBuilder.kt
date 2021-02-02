package ru.rbs.mobile.payment.sdk

import android.content.Context
import ru.rbs.mobile.payment.sdk.component.CardInfoProvider
import ru.rbs.mobile.payment.sdk.component.KeyProvider
import ru.rbs.mobile.payment.sdk.component.impl.CachedKeyProvider
import ru.rbs.mobile.payment.sdk.component.impl.RemoteCardInfoProvider
import ru.rbs.mobile.payment.sdk.component.impl.RemoteKeyProvider
import ru.rbs.mobile.payment.sdk.model.SDKConfig

/**
 * Конструктор для формирования конфигурации SDK.
 *
 * @param context контекст приложения.
 */
class SDKConfigBuilder(context: Context) {

    private var keyProvider: KeyProvider = CachedKeyProvider(
        RemoteKeyProvider("https://web.rbsdev.com/soyuzpayment/keys.js"),
        context.getSharedPreferences("key", Context.MODE_PRIVATE)
    )

    private var cardInfoProvider: CardInfoProvider = RemoteCardInfoProvider(
        url = "https://mrbin.io/bins/display",
        urlBin = "https://mrbin.io/bins/"
    )

    /**
     * Изменение провайдера ключа шифрования.
     *
     * @param provider провайдер ключа шифрования для использования.
     */
    fun keyProvider(provider: KeyProvider): SDKConfigBuilder = apply {
        this.keyProvider = provider
    }

    /**
     * Изменение провайдера информации о карте.
     *
     * @param provider провайдер для получения информации о стиле и типе карты.
     */
    fun cardInfoProvider(provider: CardInfoProvider): SDKConfigBuilder = apply {
        this.cardInfoProvider = provider
    }

    /**
     * Создает конфигурацию SDK оплаты.
     *
     * @return конфигурация SDK.
     */
    fun build() = SDKConfig(
        keyProvider = this.keyProvider,
        cardInfoProvider = this.cardInfoProvider
    )
}
