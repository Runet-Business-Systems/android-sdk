package ru.rbs.mobile.payment.sdk.model

import java.io.Serializable

/**
 * Результат формирования криптограммы.
 *
 * @param status состояние.
 * @param cryptogram сформированная криптограмма.
 * @param info информация о способе оплаты.
 */
data class PaymentData(
    val status: PaymentDataStatus,
    val cryptogram: String,
    val info: PaymentInfo? = null
) : Serializable
