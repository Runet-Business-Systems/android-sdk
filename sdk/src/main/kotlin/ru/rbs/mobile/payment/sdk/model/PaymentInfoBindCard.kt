package ru.rbs.mobile.payment.sdk.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Данные о платеже связанной картой.
 *
 * @param order идентификатор оплаченного заказа.
 * @param bindingId идентификатор связанной карты, используемый при оплате.
 */
data class PaymentInfoBindCard(
    val order: String,
    val bindingId: String
) : PaymentInfo {

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(order)
        writeString(bindingId)
    }

    companion object {

        /**
         * Объект для создания [PaymentInfoBindCard] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<PaymentInfoBindCard> =
            object : Parcelable.Creator<PaymentInfoBindCard> {
                override fun createFromParcel(source: Parcel): PaymentInfoBindCard =
                    PaymentInfoBindCard(source)

                override fun newArray(size: Int): Array<PaymentInfoBindCard?> = arrayOfNulls(size)
            }
    }
}
