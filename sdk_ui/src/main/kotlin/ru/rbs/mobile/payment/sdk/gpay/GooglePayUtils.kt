package ru.rbs.mobile.payment.sdk.gpay

import android.content.Context
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import org.json.JSONObject
import ru.rbs.mobile.payment.sdk.utils.deviceHasGooglePlayServices

/**
 * Вспомогательный класс для формирования запроса на проведения платежа через Google Pay.
 */
@Suppress("TooManyFunctions")
object GooglePayUtils {

    /**
     * Метод создания клиента для проведения платежа через Google Pay.
     *
     * @param context контекст приложения.
     * @param environment окружение, в котором будет работать клиент.
     * @return PaymentsClient .
     */
    fun createPaymentsClient(
        context: Context,
        environment: Int
    ): PaymentsClient = Wallet.getPaymentsClient(
        context,
        Wallet.WalletOptions.Builder()
            .setEnvironment(environment)
            .build()
    )

    /**
     * Метод для проверки возможность выполнить платеж через Google Pay.
     *
     * @param context контекст приложения.
     * @param isReadyToPayJson json с описанием платежа.
     * @param paymentsClient клиент для выполнения платежа.
     * @param callback слушатель для получения результата выполнения проверки.
     */
    fun possiblyShowGooglePayButton(
        context: Context,
        isReadyToPayJson: JSONObject,
        paymentsClient: PaymentsClient,
        callback: GooglePayCheckCallback
    ) {
        val servicesAvailable = deviceHasGooglePlayServices(context)
        if (servicesAvailable) {
            val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString())
            paymentsClient.isReadyToPay(request).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    callback.onReadyToRequest()
                } else {
                    callback.onNotReadyToRequest()
                }
            }
        } else {
            callback.onNoGooglePlayServices()
        }
    }

    /**
     * Интерфейс слушателя для получения результата проверки возможности выполнить платеж через
     * Google Pay на устройстве пользователя.
     */
    interface GooglePayCheckCallback {

        /**
         * Вызывается в случае отсутствия Google Play сервисов на устройстве пользователя.
         */
        fun onNoGooglePlayServices()

        /**
         * Вызывается в случае отсутствия возможности выполнить платеж через Google Pay.
         */
        fun onNotReadyToRequest()

        /**
         * Вызывается в случае наличия возможности выполнить платеж через Google Pay.
         */
        fun onReadyToRequest()
    }
}
