package ru.rbs.mobile.payment.sdk.gpay

/**
 * Возможные способы получения оплаты по картам.
 *
 * @param value значение, передаваемое в Google Pay.
 */
enum class GooglePayCardNetwork(val value: String) {
    AMEX("AMEX"),
    DISCOVER("DISCOVER"),
    INTERAC("INTERAC"),
    JCB("JCB"),
    MASTERCARD("MASTERCARD"),
    VISA("VISA")
}
