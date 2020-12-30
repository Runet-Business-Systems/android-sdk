package ru.rbs.mobile.payment.sdk.gpay

/**
 * Возможные варианты проведения оплаты.
 *
 * @param value значение, передаваемое в Google Pay.
 */
enum class GooglePayCheckoutOption(val value: String) {
    COMPLETE_IMMEDIATE_PURCHASE("COMPLETE_IMMEDIATE_PURCHASE")
}
