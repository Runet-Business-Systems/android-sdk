package ru.rbs.mobile.payment.sdk.core.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Идентификатор карты.
 *
 * @param value Значение идентификатора
 */
sealed class CardIdentifier(open val value: String) : Parcelable {

    companion object {

        /**
         * Метод для записи идентификатора карты в Parcel.
         */
        fun Parcel.writeCardIdentifier(cardIdentifier: CardIdentifier, flags: Int) {
            when (cardIdentifier) {
                is CardPanIdentifier -> {
                    writeString("CardPanIdentifier")
                    writeParcelable(cardIdentifier, flags)
                }
                is CardBindingIdIdentifier -> {
                    writeString("CardBindingIdIdentifier")
                    writeParcelable(cardIdentifier, flags)
                }
            }
        }

        /**
         * Метод для чтения идентификатора карты из Parcel.
         */
        fun Parcel.readCardIdentifier(): CardIdentifier {
            return when (readString()) {
                "CardPanIdentifier" -> readParcelable<CardPanIdentifier>(CardPanIdentifier::class.java.classLoader)!!
                "CardBindingIdIdentifier" -> readParcelable<CardBindingIdIdentifier>(
                    CardBindingIdIdentifier::class.java.classLoader
                )!!
                else -> throw IllegalArgumentException("Unknown type of card identifier for read from parcel")
            }
        }
    }
}

/**
 * Идентификатор по номеру карты.
 *
 * @param value номер карты.
 */
data class CardPanIdentifier(override val value: String) : CardIdentifier(value) {

    constructor(source: Parcel) : this(
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(value)
    }

    companion object {

        /**
         * Объект для создания [CardPanIdentifier] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<CardPanIdentifier> =
            object : Parcelable.Creator<CardPanIdentifier> {
                override fun createFromParcel(source: Parcel): CardPanIdentifier =
                    CardPanIdentifier(source)

                override fun newArray(size: Int): Array<CardPanIdentifier?> = arrayOfNulls(size)
            }
    }
}

/**
 * Идентификатор по номеру привязки.
 *
 * @param value номер привязки.
 */
data class CardBindingIdIdentifier(override val value: String) : CardIdentifier(value) {

    constructor(source: Parcel) : this(
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(value)
    }

    companion object {

        /**
         * Объект для создания [CardBindingIdIdentifier] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<CardBindingIdIdentifier> =
            object : Parcelable.Creator<CardBindingIdIdentifier> {
                override fun createFromParcel(source: Parcel): CardBindingIdIdentifier =
                    CardBindingIdIdentifier(source)

                override fun newArray(size: Int): Array<CardBindingIdIdentifier?> =
                    arrayOfNulls(size)
            }
    }
}
