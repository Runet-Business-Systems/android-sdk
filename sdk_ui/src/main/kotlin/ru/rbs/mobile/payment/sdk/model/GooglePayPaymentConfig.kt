package ru.rbs.mobile.payment.sdk.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.wallet.PaymentDataRequest
import ru.rbs.mobile.payment.sdk.utils.readBooleanValue
import ru.rbs.mobile.payment.sdk.utils.writeBooleanValue
import java.util.*

/**
 * Конфигурация оплаты через кнопку Google pay.
 *
 * @param order идентификатор заказа для оплаты.
 * @param uuid идентификатор оплаты.
 * @param theme настройка темы интерфейса.
 * @param locale локаль, в которой должна работать форма оплаты.
 * @param timestamp время оплаты.
 * @param paymentData информация для проведения платежа.
 * @param testEnvironment флаг для выполнения платежа в тестовом окружении.
 */
data class GooglePayPaymentConfig internal constructor(
    val order: String,
    val uuid: String,
    val theme: Theme,
    val locale: Locale,
    val timestamp: Long,
    val paymentData: PaymentDataRequest,
    val testEnvironment: Boolean
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        Theme.values()[source.readInt()],
        source.readSerializable() as Locale,
        source.readLong(),
        source.readParcelable<PaymentDataRequest>(PaymentDataRequest::class.java.classLoader)!!,
        source.readBooleanValue()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(order)
        writeString(uuid)
        writeInt(theme.ordinal)
        writeSerializable(locale)
        writeLong(timestamp)
        writeParcelable(paymentData, flags)
        writeBooleanValue(testEnvironment)
    }

    companion object {

        /**
         * Объект для создания [GooglePayPaymentConfig] из данных в Parcel.
         */
        @JvmField
        val CREATOR: Parcelable.Creator<GooglePayPaymentConfig> =
            object : Parcelable.Creator<GooglePayPaymentConfig> {
                override fun createFromParcel(source: Parcel): GooglePayPaymentConfig =
                    GooglePayPaymentConfig(source)

                override fun newArray(size: Int): Array<GooglePayPaymentConfig?> =
                    arrayOfNulls(size)
            }
    }
}
