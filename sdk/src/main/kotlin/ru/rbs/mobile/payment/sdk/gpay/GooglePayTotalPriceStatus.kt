package ru.rbs.mobile.payment.sdk.gpay

/**
 * Возможные варианты итоговой цены.
 *
 * @param value значение, передаваемое в Google Pay.
 */
enum class GooglePayTotalPriceStatus(val value: String) {
    FINAL("FINAL")
}
