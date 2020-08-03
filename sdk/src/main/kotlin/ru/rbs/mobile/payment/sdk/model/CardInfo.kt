package ru.rbs.mobile.payment.sdk.model

import java.io.Serializable

/**
 * Модель информации о платежной карте, используемой при оплате.
 *
 * @param identifier идентификатор карты.
 * @param expDate дата завершения действия карты.
 * @param cvv код безопасности.
 */
data class CardInfo(
    val identifier: CardIdentifier,
    val expDate: ExpiryDate? = null,
    val cvv: Int? = null
): Serializable
