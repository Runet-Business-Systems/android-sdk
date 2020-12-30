package ru.rbs.mobile.payment.sample.kotlin.threeds

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bpcbt.threeds2.android.impl.Factory
import com.bpcbt.threeds2.android.impl.pojo.SdkEphemPubKey
import com.bpcbt.threeds2.android.spec.ChallengeStatusReceiver
import com.bpcbt.threeds2.android.spec.CompletionEvent
import com.bpcbt.threeds2.android.spec.ProtocolErrorEvent
import com.bpcbt.threeds2.android.spec.RuntimeErrorEvent
import com.bpcbt.threeds2.android.spec.ThreeDS2Service
import com.bpcbt.threeds2.android.spec.Transaction
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_three_d_s.amount
import kotlinx.android.synthetic.main.activity_three_d_s.baseUrl
import kotlinx.android.synthetic.main.activity_three_d_s.email
import kotlinx.android.synthetic.main.activity_three_d_s.failUrl
import kotlinx.android.synthetic.main.activity_three_d_s.password
import kotlinx.android.synthetic.main.activity_three_d_s.returnUrl
import kotlinx.android.synthetic.main.activity_three_d_s.text
import kotlinx.android.synthetic.main.activity_three_d_s.threeDSCheckout
import kotlinx.android.synthetic.main.activity_three_d_s.userName
import ru.rbs.mobile.payment.sample.kotlin.R
import ru.rbs.mobile.payment.sample.kotlin.helpers.launchGlobalScope
import ru.rbs.mobile.payment.sample.kotlin.helpers.log
import ru.rbs.mobile.payment.sample.kotlin.threeds.ThreeDSGatewayApi.PaymentCheckOrderStatusRequest
import ru.rbs.mobile.payment.sample.kotlin.threeds.ThreeDSGatewayApi.PaymentFinishOrderRequest
import ru.rbs.mobile.payment.sample.kotlin.threeds.ThreeDSGatewayApi.PaymentOrderRequest
import ru.rbs.mobile.payment.sdk.PaymentConfigBuilder
import ru.rbs.mobile.payment.sdk.ResultCallback
import ru.rbs.mobile.payment.sdk.SDKConfigBuilder
import ru.rbs.mobile.payment.sdk.SDKException
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.component.impl.RemoteKeyProvider
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.model.PaymentInfoNewCard

class ThreeDSActivity : AppCompatActivity() {

    // Api для проведения тестового платежа.
    private val api = ThreeDSGatewayApi()

    // Поля необходимы для создания и запуска 3DS Challenge Flow.
    private val factory = Factory()
    private lateinit var threeDS2Service: ThreeDS2Service
    private var transaction: Transaction? = null

    // Настраиваемые параметры примера из UI.
    private val argBaseUrl: String
        get() = baseUrl.text.toString()
    private val argUserName: String
        get() = userName.text.toString()
    private val argPassword: String
        get() = password.text.toString()
    private val argText: String
        get() = text.text.toString()
    private val argAmount: String
        get() = amount.text.toString()
    private val argEmail: String
        get() = email.text.toString()
    private val argReturnUrl: String
        get() = returnUrl.text.toString()
    private val argFailUrl: String
        get() = failUrl.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_d_s)
        SDKPayment.init(
            this, SDKConfigBuilder(this)
                // Настройка получения ключа для шифрования seToken.
                .keyProvider(RemoteKeyProvider(url = "$argBaseUrl/keys.js"))
                .build()
        )

        threeDSCheckout.setOnClickListener {
            registerOrder()
        }
    }

    /**
     * Регистрация заказа для начала процесса оплаты.
     */
    private fun registerOrder() = launchGlobalScope {
        // Получаем идентификатор заказа.
        val registerResponse = api.executeRegisterOrder(
            url = "$argBaseUrl/rest/register.do",
            request = ThreeDSGatewayApi.RegisterRequest(
                amount = argAmount,
                userName = argUserName,
                password = argPassword,
                returnUrl = argReturnUrl,
                failUrl = argFailUrl,
                email = argEmail
            )
        )
        // Формируем параметры для запуска экрана ввода данных карты и формирования seToken по
        // завершению заполнения данных.
        val paymentConfig = PaymentConfigBuilder(registerResponse.orderId)
            .build()

        // Вызов экрана оплаты (заполнения данных карты).
        SDKPayment.cryptogram(this@ThreeDSActivity, paymentConfig)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Обработка результата формирования seToken.
        SDKPayment.handleResult(requestCode, data, object : ResultCallback<PaymentData> {

            override fun onSuccess(result: PaymentData) {
                // Результат формирования seToken (криптограммы).
                when {
                    result.status.isSucceeded() -> {
                        val info = result.info
                        if (info is PaymentInfoNewCard) {
                            executeThreeDSChallengeFlow(
                                seToken = result.cryptogram,
                                mdOrder = info.order
                            )
                        }
                        log("$result")
                    }
                    result.status.isCanceled() -> {
                        log("canceled")
                    }
                }
            }

            override fun onFail(e: SDKException) {
                // Возникла ошибка.
                log("${e.message} ${e.cause}")
            }
        })
    }

    /**
     * Запуск процесса Challenge Flow.
     *
     * @param seToken токен (криптограма) с идентификатором заказа и информацией об платежной карте.
     * @param mdOrder идентификатор заказа.
     */
    private fun executeThreeDSChallengeFlow(
        seToken: String,
        mdOrder: String
    ) = launchGlobalScope {
        threeDS2Service = factory.newThreeDS2Service()
        val configParams = factory.newConfigParameters()
        val uiCustomization = factory.newUiCustomization()
        threeDS2Service.initialize(
            this@ThreeDSActivity,
            configParams,
            "en-US",
            uiCustomization
        )

        // Начало оплаты заказа. Получение идентификатора транзакции 3DS сервера.
        val paymentOrderResponse = api.executePaymentOrder(
            url = "$argBaseUrl/rest/paymentorder.do",
            request = PaymentOrderRequest(
                seToken = seToken,
                mdOrder = mdOrder,
                userName = argUserName,
                password = argPassword,
                text = argText,
                threeDSSDK = true
            )
        )

        transaction?.close() // Закрываем предыдущую транзакцию если она была.

        transaction = threeDS2Service.createTransaction("F000000000", "2.1.0")

        //  Пример создания транзакции с шифрованием deviceInfo переданным RSA ключом.
        //  val rsaPem: String = ...
        //  transaction = threeDS2Service.createTransactionWithRSADSKey(
        //      rsaPem,
        //      "2.1.0"
        //   )

        //   Пример создания транзакции с шифрованием deviceInfo переданным EC ключом.
        //   val ecPem: String = ""
        //   val directoryServerID: String = ""
        //   transaction = threeDS2Service.createTransactionWithECDSKey(
        //       ecPem,
        //       directoryServerID,
        //       "2.1.0"
        //    )

        // Доступные данные, для отправки на платежный шлюз
        val authRequestParams = transaction!!.authenticationRequestParameters!!
        val encryptedDeviceInfo: String = authRequestParams.deviceData
        val sdkTransactionID: String = authRequestParams.sdkTransactionID
        val sdkAppId: String = authRequestParams.sdkAppID
        val sdkEphmeralPublicKey: String = authRequestParams.sdkEphemeralPublicKey
        val sdkReferenceNumber: String = authRequestParams.sdkReferenceNumber

        // Передаем только необходимые поля из sdkEphmeralPublicKey
        val gson = Gson()
        val sdkEphemPubKey: String =
            gson.toJson(gson.fromJson(sdkEphmeralPublicKey, SdkEphemPubKey::class.java))

        // Получаем необходимую информацию для запуска Challenge Flow (acsSignedContent,
        // acsTransactionId, acsRefNumber).
        val paymentOrderSecondStepResponse = api.executePaymentOrderSecondStep(
            url = "$argBaseUrl/rest/paymentorder.do",
            request = ThreeDSGatewayApi.PaymentOrderSecondStepRequest(
                seToken = seToken,
                mdOrder = mdOrder,
                userName = argUserName,
                password = argPassword,
                text = argText,
                threeDSSDK = true,
                threeDSServerTransId = paymentOrderResponse.threeDSServerTransId,
                threeDSSDKEncData = encryptedDeviceInfo,
                threeDSSDKEphemPubKey = sdkEphemPubKey,
                threeDSSDKAppId = sdkAppId,
                threeDSSDKTransId = sdkTransactionID,
                threeDSSDKReferenceNumber = sdkReferenceNumber
            )
        )

        val challengeParameters = factory.newChallengeParameters()

        // Параметры для запуска Challenge Flow.
        challengeParameters.acsTransactionID =
            paymentOrderSecondStepResponse.threeDSAcsTransactionId
        challengeParameters.acsRefNumber = paymentOrderSecondStepResponse.threeDSAcsRefNumber
        challengeParameters.acsSignedContent =
            paymentOrderSecondStepResponse.threeDSAcsSignedContent
        challengeParameters.set3DSServerTransactionID(paymentOrderResponse.threeDSServerTransId)

        // Слушатель для обработки процесса выполнения Challenge Flow.
        val challengeStatusReceiver: ChallengeStatusReceiver = object : ChallengeStatusReceiver {
            override fun cancelled() {
                log("cancelled")
                cleanup()
            }

            override fun protocolError(protocolErrorEvent: ProtocolErrorEvent) {
                log("protocolError $protocolErrorEvent")
                cleanup()
            }

            override fun runtimeError(runtimeErrorEvent: RuntimeErrorEvent) {
                log("runtimeError $runtimeErrorEvent")
                cleanup()
            }

            override fun completed(completionEvent: CompletionEvent) {
                log("completed $completionEvent")
                cleanup()
                if (completionEvent.transactionStatus == "Y") {
                    launchGlobalScope {
                        finishOrder(tDsTransId = paymentOrderResponse.threeDSServerTransId).join()
                        checkOrderStatus(orderId = mdOrder)
                    }
                }
            }

            override fun timedout() {
                log("timedout")
                cleanup()
            }
        }
        val timeOut = 5

        // Запуск Challenge Flow.
        transaction!!.doChallenge(
            this@ThreeDSActivity,
            challengeParameters,
            challengeStatusReceiver,
            timeOut
        )
    }

    /**
     * Завершение процесса оплаты.
     *
     * @param tDsTransId идентификатор транзакции на 3DS сервере.
     */
    private fun finishOrder(tDsTransId: String) = launchGlobalScope {
        try {
            api.executeFinishOrder(
                url = "$argBaseUrl/rest/finish3dsVer2.do",
                request = PaymentFinishOrderRequest(
                    tDsTransId = tDsTransId
                )
            )
        } catch (e: Exception) {
            // TODO нужны доработки метода finish3dsVer2.do
        }
    }

    /**
     * Проверка статуса заказа.
     *
     * @param orderId идентификатор заказа.
     */
    private fun checkOrderStatus(
        orderId: String
    ) = launchGlobalScope {
        val status = api.executeCheckOrderStatus(
            url = "$argBaseUrl/rest/getOrderStatusExtended.do",
            request = PaymentCheckOrderStatusRequest(
                orderId = orderId,
                userName = argUserName,
                password = argPassword
            )
        )
        log(status)
    }

    private fun cleanup() {
        transaction?.close()
        threeDS2Service.cleanup(this@ThreeDSActivity)
    }
}
