package ru.rbs.mobile.payment.sdk

/**
 * Глобальные константы используемые в SDK.
 */
object Constants {
    internal const val REQUEST_CODE_CRYPTOGRAM = 60000
    internal const val REQUEST_CODE_SCAN_CARD = 60001
    internal const val REQUEST_CODE_GPAY_LOAD_PAYMENT_DATA = 60002
    internal const val INTENT_EXTRA_CONFIG = "payment.sdk.config"
    internal const val INTENT_EXTRA_CARD = "payment.sdk.card"
    internal const val INTENT_EXTRA_RESULT = "payment.sdk.result"
    internal const val INTENT_EXTRA_ERROR = "payment.sdk.error"
}
