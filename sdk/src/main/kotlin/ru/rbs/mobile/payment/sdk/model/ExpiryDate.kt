package ru.rbs.mobile.payment.sdk.model

import java.io.Serializable

/**
 * Модель описания завершения даты действия карты.
 *
 * @param expYear год в формате yyyy.
 * @param expMonth месяц в формате mm.
 */
data class ExpiryDate(
    val expYear: Int,
    val expMonth: Int
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpiryDate

        if (expYear != other.expYear) return false
        if (expMonth != other.expMonth) return false

        return true
    }

    override fun hashCode(): Int {
        var result = expYear
        result = 31 * result + expMonth
        return result
    }
}
