package ru.rbs.mobile.payment.sdk.model

import ru.rbs.mobile.payment.sdk.component.CardInfoProvider
import ru.rbs.mobile.payment.sdk.component.KeyProvider

/**
 * Класс параметров конфигурации SDK.
 *
 * @param keyProvider используемый провайдер ключа шифрования.
 * @param cardInfoProvider используемый провайдер информации о стиле и типе карты.
 */
data class SDKConfig(
    val keyProvider: KeyProvider,
    val cardInfoProvider: CardInfoProvider
)
