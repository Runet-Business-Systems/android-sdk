package ru.rbs.mobile.payment.sdk.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.WalletConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.rbs.mobile.payment.sdk.Constants
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.component.CryptogramProcessor
import ru.rbs.mobile.payment.sdk.gpay.GooglePayUtils
import ru.rbs.mobile.payment.sdk.model.GooglePayPaymentConfig
import ru.rbs.mobile.payment.sdk.model.PaymentDataStatus
import ru.rbs.mobile.payment.sdk.model.PaymentInfoGooglePay
import ru.rbs.mobile.payment.sdk.utils.finishWithResult
import com.google.android.gms.wallet.PaymentData as GPaymentData

/**
 * Экран для запуска процесса оплаты через Google Pay.
 */
class GooglePayActivity : BaseActivity() {

    private var cryptogramProcessor: CryptogramProcessor = SDKPayment.cryptogramProcessor
    private val config: GooglePayPaymentConfig by lazy {
        intent.getParcelableExtra<GooglePayPaymentConfig>(Constants.INTENT_EXTRA_CONFIG) as GooglePayPaymentConfig
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val environment = if (config.testEnvironment) {
            WalletConstants.ENVIRONMENT_TEST
        } else {
            WalletConstants.ENVIRONMENT_PRODUCTION
        }
        val paymentsClient = GooglePayUtils.createPaymentsClient(this, environment)
        requestPayment(
            paymentsClient = paymentsClient,
            activity = this,
            config = config
        )
    }

    private fun requestPayment(
        paymentsClient: PaymentsClient,
        activity: Activity,
        config: GooglePayPaymentConfig
    ) {
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(config.paymentData),
            activity, Constants.REQUEST_CODE_GPAY_LOAD_PAYMENT_DATA
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.REQUEST_CODE_GPAY_LOAD_PAYMENT_DATA -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val paymentData: GPaymentData? = GPaymentData.getFromIntent(data!!)
                        paymentData?.let {
                            val json = it.toJson()
                            val paymentMethodData = JSONObject(json)
                                .getJSONObject("paymentMethodData")
                            val paymentToken = paymentMethodData
                                .getJSONObject("tokenizationData")
                                .getString("token")

                            handlePaymentData(paymentToken)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        finish()
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        finish()
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handlePaymentData(token: String) {
        workScope.launch(Dispatchers.Main) {
            val cryptogram = cryptogramProcessor.create(token)
            finishWithResult(
                ru.rbs.mobile.payment.sdk.model.PaymentData(
                    status = PaymentDataStatus.SUCCEEDED,
                    cryptogram = cryptogram,
                    info = PaymentInfoGooglePay(
                        order = config.order
                    )
                )
            )
        }
    }

    companion object {

        /**
         * Подготавливает [Intent] для запуска экрана оплаты через Google Pay.
         *
         * @param context для подготовки intent.
         * @param config конфигурация оплаты.
         */
        fun prepareIntent(
            context: Context,
            config: GooglePayPaymentConfig
        ): Intent = Intent(context, GooglePayActivity::class.java).apply {
            putExtra(Constants.INTENT_EXTRA_CONFIG, config)
        }
    }
}
