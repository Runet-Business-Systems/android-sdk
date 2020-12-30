package ru.rbs.mobile.payment.sdk.core.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Модель описания завершения даты действия карты.
 *
 * @param expYear год в формате yyyy.
 * @param expMonth месяц в формате mm.
 */
data class ExpiryDate(
    val expYear: Int,
    val expMonth: Int
) : Parcelable {

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

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(expYear)
        writeInt(expMonth)
    }

    companion object {

        /**
         * Объект для создания [ExpiryDate] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<ExpiryDate> = object : Parcelable.Creator<ExpiryDate> {
            override fun createFromParcel(source: Parcel): ExpiryDate = ExpiryDate(source)
            override fun newArray(size: Int): Array<ExpiryDate?> = arrayOfNulls(size)
        }
    }
}
