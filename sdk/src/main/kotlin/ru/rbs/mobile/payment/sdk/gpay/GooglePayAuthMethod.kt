package ru.rbs.mobile.payment.sdk.gpay

/**
 * Возможные методы авторизации.
 *
 * @param value значение, передаваемое в Google Pay.
 */
enum class GooglePayAuthMethod(val value: String) {
    PAN_ONLY("PAN_ONLY"),
    CRYPTOGRAM_3DS("CRYPTOGRAM_3DS")
}
