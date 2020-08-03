package ru.rbs.mobile.payment.sdk.model

/**
 * Данные о платеже новой картой.
 *
 * @param saveCard выбор пользователя - true если он хочет сохранить карту, в противном случае false.
 * @param holder указанное имя владельца карты.
 */
data class PaymentInfoNewCard(
    val saveCard: Boolean,
    val holder: String
) : PaymentInfo
