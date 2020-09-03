package ru.rbs.mobile.payment.sdk.gpay


/**
 * Возможные варианты спецификации.
 *
 * @param value значение, передаваемое в Google Pay.
 */
enum class GoogleTokenizationSpecificationType(val value: String) {
    PAYMENT_GATEWAY("PAYMENT_GATEWAY")
}
