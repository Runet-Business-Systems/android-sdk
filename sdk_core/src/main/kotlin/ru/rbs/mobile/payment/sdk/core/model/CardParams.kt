package ru.rbs.mobile.payment.sdk.core.model

/**
 *  Информация о карте.
 *
 *  @param mdOrder номер заказа.
 *  @param pan номер карты.
 *  @param cvc секретный код карты.
 *  @param expiryMMYY срок действия карты.
 *  @param cardHolder владелец карты.
 *  @param pubKey публичный ключ.
 * */

data class CardParams(
    val mdOrder: String,
    val pan: String,
    val cvc: String,
    val expiryMMYY: String,
    val cardHolder: String?,
    val pubKey: String
)
