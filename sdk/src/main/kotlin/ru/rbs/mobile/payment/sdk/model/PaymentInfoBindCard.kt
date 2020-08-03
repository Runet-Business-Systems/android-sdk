package ru.rbs.mobile.payment.sdk.model

/**
 * Данные о платеже связанной картой.
 *
 * @param bindingId идентификатор связанной карты, используемый при оплате.
 */
data class PaymentInfoBindCard(
    val bindingId: String
) : PaymentInfo
