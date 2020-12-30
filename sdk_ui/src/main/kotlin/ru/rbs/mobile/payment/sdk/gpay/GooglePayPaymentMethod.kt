package ru.rbs.mobile.payment.sdk.gpay

/**
 * Возможные варианты способа оплаты.
 *
 * @param value значение, передаваемое в Google Pay.
 */
enum class GooglePayPaymentMethod(val value: String) {
    CARD("CARD")
}
