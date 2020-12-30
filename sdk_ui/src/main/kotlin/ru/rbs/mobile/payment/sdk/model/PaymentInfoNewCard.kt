package ru.rbs.mobile.payment.sdk.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Данные о платеже новой картой.
 *
 * @param order идентификатор оплаченного заказа.
 * @param saveCard выбор пользователя - true если он хочет сохранить карту, в противном случае false.
 * @param holder указанное имя владельца карты.
 */
data class PaymentInfoNewCard(
    val order: String,
    val saveCard: Boolean,
    val holder: String
) : PaymentInfo, Parcelable {

    constructor(source: Parcel) : this(
        source.readString()!!,
        1 == source.readInt(),
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(order)
        writeInt((if (saveCard) 1 else 0))
        writeString(holder)
    }

    companion object {

        /**
         * Объект для создания [PaymentInfoNewCard] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<PaymentInfoNewCard> =
            object : Parcelable.Creator<PaymentInfoNewCard> {
                override fun createFromParcel(source: Parcel): PaymentInfoNewCard =
                    PaymentInfoNewCard(source)

                override fun newArray(size: Int): Array<PaymentInfoNewCard?> = arrayOfNulls(size)
            }
    }
}
