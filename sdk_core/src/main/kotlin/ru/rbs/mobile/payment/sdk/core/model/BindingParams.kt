package ru.rbs.mobile.payment.sdk.core.model

/**
 *  Информация о сохраненной карте.
 *
 *  @param mdOrder номер заказа.
 *  @param bindingID номер сохраненной карты.
 *  @param cvc секретный код карты.
 *  @param pubKey публичный ключ.
 */

data class BindingParams(
    val mdOrder: String,
    val bindingID: String,
    val cvc: String?,
    val pubKey: String
)
