package ru.rbs.mobile.payment.sdk.model

import android.os.Parcel
import android.os.Parcelable
import ru.rbs.mobile.payment.sdk.model.Card.Companion.readCards
import ru.rbs.mobile.payment.sdk.model.Card.Companion.writeCards
import java.util.*

/**
 * Конфигурация оплаты.
 *
 * @param order идентификатор заказа для оплаты.
 * @param cardSaveOptions настройка опции привязки новой карты после оплаты.
 * @param holderInputOptions настройка опции ввода владельца карты.
 * @param cameraScannerOptions настройка опции сканирования данных карты через камеру.
 * @param nfcScannerOptions настройка опции сканирования данных карты через NFC.
 * @param theme настройка темы интерфейса.
 * @param cards список привязанных карт.
 * @param uuid идентификатор оплаты.
 * @param timestamp время оплаты.
 * @param locale локаль, в которой должна работать форма оплаты.
 * @param buttonText текст кнопки оплаты.
 * @param bindingCVCRequired обязательный ввод CVC оплачивая ранее сохраненной картой.
 */
data class PaymentConfig internal constructor(
    val order: String,
    val cardSaveOptions: CardSaveOptions,
    val holderInputOptions: HolderInputOptions,
    val cameraScannerOptions: CameraScannerOptions,
    val theme: Theme,
    val nfcScannerOptions: NfcScannerOptions,
    val cards: Set<Card>,
    val uuid: String,
    val timestamp: Long,
    val locale: Locale,
    val buttonText: String?,
    val bindingCVCRequired: Boolean
) : Parcelable {

    constructor(source: Parcel) :
            this(
                source.readString()!!,
                CardSaveOptions.values()[source.readInt()],
                HolderInputOptions.values()[source.readInt()],
                CameraScannerOptions.values()[source.readInt()],
                Theme.values()[source.readInt()],
                NfcScannerOptions.values()[source.readInt()],
                source.readCards(),
                source.readString()!!,
                source.readLong(),
                source.readSerializable() as Locale,
                source.readString(),
                1 == source.readInt()
            )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(order)
        writeInt(cardSaveOptions.ordinal)
        writeInt(holderInputOptions.ordinal)
        writeInt(cameraScannerOptions.ordinal)
        writeInt(theme.ordinal)
        writeInt(nfcScannerOptions.ordinal)
        writeCards(cards, flags)
        writeString(uuid)
        writeLong(timestamp)
        writeSerializable(locale)
        writeString(buttonText)
        writeInt((if (bindingCVCRequired) 1 else 0))
    }

    companion object {

        /**
         * Объект для создания [PaymentConfig] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<PaymentConfig> =
            object : Parcelable.Creator<PaymentConfig> {
                override fun createFromParcel(source: Parcel): PaymentConfig = PaymentConfig(source)
                override fun newArray(size: Int): Array<PaymentConfig?> = arrayOfNulls(size)
            }
    }
}
