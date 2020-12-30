package ru.rbs.mobile.payment.sdk.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Данные о платеже через Google Pay.
 *
 * @param order идентификатор оплаченного заказа.
 */
data class PaymentInfoGooglePay(
    val order: String
) : PaymentInfo{

    constructor(source: Parcel) : this(
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(order)
    }

    companion object {

        /**
         * Объект для создания [PaymentInfoGooglePay] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<PaymentInfoGooglePay> =
            object : Parcelable.Creator<PaymentInfoGooglePay> {
                override fun createFromParcel(source: Parcel): PaymentInfoGooglePay =
                    PaymentInfoGooglePay(source)

                override fun newArray(size: Int): Array<PaymentInfoGooglePay?> = arrayOfNulls(size)
            }
    }
}
