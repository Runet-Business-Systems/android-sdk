package ru.rbs.mobile.payment.sdk.model

import java.io.Serializable

/**
 * Описание ранее сохраненной карты.
 *
 * @param pan номер карты с маской.
 * @param bindingId идентификатор связки.
 * @param expiryDate срок действия карты.
 */
data class Card(
    val pan: String,
    val bindingId: String,
    val expiryDate: ExpiryDate? = null
): Serializable
