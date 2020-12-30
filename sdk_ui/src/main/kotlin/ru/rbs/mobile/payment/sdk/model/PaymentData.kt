package ru.rbs.mobile.payment.sdk.model

import android.os.Parcel
import android.os.Parcelable

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
) : Parcelable {

    constructor(source: Parcel) : this(
        PaymentDataStatus.values()[source.readInt()],
        source.readString()!!,
        source.readParcelable<PaymentInfo>(PaymentInfo::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(status.ordinal)
        writeString(cryptogram)
        writeParcelable(info, 0)
    }

    companion object {

        /**
         * Объект для создания [PaymentData] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<PaymentData> = object : Parcelable.Creator<PaymentData> {
            override fun createFromParcel(source: Parcel): PaymentData = PaymentData(source)
            override fun newArray(size: Int): Array<PaymentData?> = arrayOfNulls(size)
        }
    }
}
