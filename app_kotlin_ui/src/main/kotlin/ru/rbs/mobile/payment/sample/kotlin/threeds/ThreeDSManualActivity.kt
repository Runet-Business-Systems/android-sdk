package ru.rbs.mobile.payment.sample.kotlin.threeds

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bpcbt.threeds2.android.impl.Factory
import com.bpcbt.threeds2.android.spec.ChallengeStatusReceiver
import com.bpcbt.threeds2.android.spec.CompletionEvent
import com.bpcbt.threeds2.android.spec.ProtocolErrorEvent
import com.bpcbt.threeds2.android.spec.RuntimeErrorEvent
import com.bpcbt.threeds2.android.spec.ThreeDS2Service
import com.bpcbt.threeds2.android.spec.Transaction
import kotlinx.android.synthetic.main.activity_three_d_s_manual.acsRefNumber
import kotlinx.android.synthetic.main.activity_three_d_s_manual.acsSignedContent
import kotlinx.android.synthetic.main.activity_three_d_s_manual.acsTransactionID
import kotlinx.android.synthetic.main.activity_three_d_s_manual.executeThreeDSChallengeFlow
import kotlinx.android.synthetic.main.activity_three_d_s_manual.initThreeDSTransaction
import kotlinx.android.synthetic.main.activity_three_d_s_manual.logView
import kotlinx.android.synthetic.main.activity_three_d_s_manual.threeDSAuthenticationRequestParams
import kotlinx.android.synthetic.main.activity_three_d_s_manual.threeDSServerTransId
import ru.rbs.mobile.payment.sample.kotlin.R
import ru.rbs.mobile.payment.sample.kotlin.helpers.copyToClipboard
import ru.rbs.mobile.payment.sample.kotlin.helpers.launchGlobalScope
import ru.rbs.mobile.payment.sample.kotlin.helpers.launchMainGlobalScope
import ru.rbs.mobile.payment.sample.kotlin.helpers.log

class ThreeDSManualActivity : AppCompatActivity() {

    // Поля необходимы для создания и запуска 3DS Challenge Flow.
    private val factory = Factory()
    private lateinit var threeDS2Service: ThreeDS2Service
    private var transaction: Transaction? = null

    // Настраиваемые параметры примера из UI.
    private val argAcsTransactionID: String
        get() = acsTransactionID.text.toString()
    private val argAcsRefNumber: String
        get() = acsRefNumber.text.toString()
    private val argAcsSignedContent: String
        get() = acsSignedContent.text.toString()
    private val argThreeDSServerTransId: String
        get() = threeDSServerTransId.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_d_s_manual)
        threeDS2Service = factory.newThreeDS2Service()
        val configParams = factory.newConfigParameters()
        val uiCustomization = factory.newUiCustomization()
        threeDS2Service.initialize(
            this@ThreeDSManualActivity,
            configParams,
            "en-US",
            uiCustomization
        )
        initThreeDSTransaction.setOnClickListener {
            initThreeDSChallengeFlow()
        }
        executeThreeDSChallengeFlow.setOnClickListener {
            executeThreeDSChallengeFlow()
        }
        logView.setOnClickListener {
            copyToClipboard("3DS logs", (it as TextView).text.toString())
        }
    }

    private fun initThreeDSChallengeFlow() = launchGlobalScope {
        cleanLogView()
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


        launchMainGlobalScope {
            threeDSAuthenticationRequestParams.setText(
                """
                    sdkTransactionID:$sdkTransactionID
                    sdkAppId:$sdkAppId
                    sdkEphmeralPublicKey:$sdkEphmeralPublicKey
                    sdkReferenceNumber:$sdkReferenceNumber
                    encryptedDeviceInfo:$encryptedDeviceInfo
                """.trimIndent()
            )
            copyToClipboard(
                "3DS authentication request parameters",
                threeDSAuthenticationRequestParams.text.toString()
            )
        }
    }

    private fun executeThreeDSChallengeFlow() = launchGlobalScope {
        if (transaction == null) {
            log("Init transaction before")
            return@launchGlobalScope
        }

        val challengeParameters = factory.newChallengeParameters()

        // Параметры для запуска Challenge Flow.
        challengeParameters.acsTransactionID = argAcsTransactionID
        challengeParameters.acsRefNumber = argAcsRefNumber
        challengeParameters.acsSignedContent = argAcsSignedContent
        challengeParameters.set3DSServerTransactionID(argThreeDSServerTransId)

        // Слушатель для обработки процесса выполнения Challenge Flow.
        val challengeStatusReceiver: ChallengeStatusReceiver = object : ChallengeStatusReceiver {
            override fun cancelled() {
                log("cancelled")
                printLogView("cancelled")
                cleanup()
            }

            override fun protocolError(protocolErrorEvent: ProtocolErrorEvent) {
                log("protocolError $protocolErrorEvent")
                printLogView("protocolError $protocolErrorEvent")
                cleanup()
            }

            override fun runtimeError(runtimeErrorEvent: RuntimeErrorEvent) {
                log("runtimeError $runtimeErrorEvent")
                printLogView("runtimeError $runtimeErrorEvent")
                cleanup()
            }

            override fun completed(completionEvent: CompletionEvent) {
                log("completed $completionEvent")
                printLogView("completed $completionEvent")
                cleanup()
            }

            override fun timedout() {
                log("timedout")
                printLogView("timedout")
                cleanup()
            }
        }
        val timeOut = 5

        // Запуск Challenge Flow.
        transaction!!.doChallenge(
            this@ThreeDSManualActivity,
            challengeParameters,
            challengeStatusReceiver,
            timeOut
        )
    }

    private fun cleanLogView() {
        runOnUiThread {
            logView.text = null
        }
    }

    private fun printLogView(message: String) {
        runOnUiThread {
            logView.append("\n$message")
        }
    }

    override fun onDestroy() {
        threeDS2Service.cleanup(this@ThreeDSManualActivity)
        super.onDestroy()
    }

    private fun cleanup() {
        transaction?.close()
    }
}