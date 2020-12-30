package ru.rbs.mobile.payment.sdk.core.model

import android.os.Parcel
import android.os.Parcelable
import ru.rbs.mobile.payment.sdk.core.model.CardIdentifier.Companion.readCardIdentifier
import ru.rbs.mobile.payment.sdk.core.model.CardIdentifier.Companion.writeCardIdentifier

/**
 * Модель информации о платежной карте, используемой при оплате.
 *
 * @param identifier идентификатор карты.
 * @param expDate дата завершения действия карты.
 * @param cvv код безопасности.
 */
data class CardInfo(
    val identifier: CardIdentifier,
    val expDate: ExpiryDate? = null,
    val cvv: String? = null
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readCardIdentifier(),
        source.readParcelable<ExpiryDate?>(ExpiryDate::class.java.classLoader),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeCardIdentifier(identifier, flags)
        writeParcelable(expDate, flags)
        writeValue(cvv)
    }

    companion object {

        /**
         * Объект для создания [CardInfo] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<CardInfo> = object : Parcelable.Creator<CardInfo> {
            override fun createFromParcel(source: Parcel): CardInfo = CardInfo(source)
            override fun newArray(size: Int): Array<CardInfo?> = arrayOfNulls(size)
        }
    }
}
