package ru.rbs.mobile.payment.sdk.model

import android.os.Parcel
import android.os.Parcelable
import ru.rbs.mobile.payment.sdk.core.model.ExpiryDate

/**
 * Описание ранее сохраненной карты.
 *
 * @param pan номер карты с маской.
 * @param bindingId идентификатор связки.
 * @param expiryDate срок действия карты.
 */
data class Card(
    val pan: String,
    val bindingId: String,
    val expiryDate: ExpiryDate? = null
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readParcelable<ExpiryDate?>(ExpiryDate::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(pan)
        writeString(bindingId)
        writeParcelable(expiryDate, flags)
    }

    companion object {

        /**
         * Объект для создания [Card] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<Card> = object : Parcelable.Creator<Card> {
            override fun createFromParcel(source: Parcel): Card = Card(source)
            override fun newArray(size: Int): Array<Card?> = arrayOfNulls(size)
        }

        /**
         * Метод для чтения набор карт из Parcel.
         *
         * @return прочитанный набор карт.
         */
        fun Parcel.readCards(): Set<Card> {
            val size = readInt()
            return if (size > 0) {
                val cards = Array<Card?>(size) { null }
                readTypedArray(cards, CREATOR)
                cards.map { it!! }.toSet()
            } else {
                emptySet()
            }
        }

        /**
         * Метод для записи набор карт в Parcel.
         *
         * @param cards набор карт для записи.
         * @param flags параметры записи.
         */
        fun Parcel.writeCards(cards: Set<Card>, flags: Int) {
            val size = cards.size
            writeInt(size)
            if (size > 0) {
                writeTypedArray(cards.toTypedArray(), flags)
            }
        }
    }
}
