@file:Suppress("MagicNumber", "TooManyFunctions", "UndocumentedPublicClass")
package ru.rbs.mobile.payment.sample.kotlin

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.WalletConstants
import kotlinx.android.synthetic.main.activity_main.amountInput
import kotlinx.android.synthetic.main.activity_main.cardCameraOffButton
import kotlinx.android.synthetic.main.activity_main.cardDoneTextButton
import kotlinx.android.synthetic.main.activity_main.cardEasyButton
import kotlinx.android.synthetic.main.activity_main.cardListNotRequiredCVCButton
import kotlinx.android.synthetic.main.activity_main.cardListRequiredCVCButton
import kotlinx.android.synthetic.main.activity_main.cardNewButton
import kotlinx.android.synthetic.main.activity_main.cardNfcOffButton
import kotlinx.android.synthetic.main.activity_main.countryCodeInput
import kotlinx.android.synthetic.main.activity_main.currencyCodeInput
import kotlinx.android.synthetic.main.activity_main.darkThemeButton
import kotlinx.android.synthetic.main.activity_main.gatewayInput
import kotlinx.android.synthetic.main.activity_main.gatewayMerchantIdInput
import kotlinx.android.synthetic.main.activity_main.googlePayArea
import kotlinx.android.synthetic.main.activity_main.googlePayButtonFirst
import kotlinx.android.synthetic.main.activity_main.googlePayButtonFourth
import kotlinx.android.synthetic.main.activity_main.googlePayButtonSecond
import kotlinx.android.synthetic.main.activity_main.googlePayButtonThird
import kotlinx.android.synthetic.main.activity_main.googlePayCryptogram
import kotlinx.android.synthetic.main.activity_main.googlePayNotAvailableMessage
import kotlinx.android.synthetic.main.activity_main.lightThemeButton
import kotlinx.android.synthetic.main.activity_main.localeDeButton
import kotlinx.android.synthetic.main.activity_main.localeEnButton
import kotlinx.android.synthetic.main.activity_main.localeEsButton
import kotlinx.android.synthetic.main.activity_main.localeFrButton
import kotlinx.android.synthetic.main.activity_main.localeRuButton
import kotlinx.android.synthetic.main.activity_main.localeUkButton
import kotlinx.android.synthetic.main.activity_main.merchantIdInput
import org.json.JSONObject
import ru.rbs.mobile.payment.sdk.GooglePayConfigBuilder
import ru.rbs.mobile.payment.sdk.PaymentConfigBuilder
import ru.rbs.mobile.payment.sdk.ResultCallback
import ru.rbs.mobile.payment.sdk.SDKException
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.gpay.AllowedPaymentMethods
import ru.rbs.mobile.payment.sdk.gpay.GooglePayAuthMethod
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCardNetwork
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCheckoutOption
import ru.rbs.mobile.payment.sdk.gpay.GooglePayPaymentDataRequest
import ru.rbs.mobile.payment.sdk.gpay.GooglePayPaymentMethod
import ru.rbs.mobile.payment.sdk.gpay.GooglePayTotalPriceStatus
import ru.rbs.mobile.payment.sdk.gpay.GooglePayUtils
import ru.rbs.mobile.payment.sdk.gpay.GoogleTokenizationSpecificationType
import ru.rbs.mobile.payment.sdk.gpay.MerchantInfo
import ru.rbs.mobile.payment.sdk.gpay.PaymentMethodParameters
import ru.rbs.mobile.payment.sdk.gpay.TokenizationSpecification
import ru.rbs.mobile.payment.sdk.gpay.TokenizationSpecificationParameters
import ru.rbs.mobile.payment.sdk.gpay.TransactionInfo
import ru.rbs.mobile.payment.sdk.model.CameraScannerOptions
import ru.rbs.mobile.payment.sdk.model.Card
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions
import ru.rbs.mobile.payment.sdk.model.ExpiryDate
import ru.rbs.mobile.payment.sdk.model.GooglePayPaymentConfig
import ru.rbs.mobile.payment.sdk.model.HolderInputOptions
import ru.rbs.mobile.payment.sdk.model.NfcScannerOptions
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.model.PaymentInfoBindCard
import ru.rbs.mobile.payment.sdk.model.PaymentInfoGooglePay
import ru.rbs.mobile.payment.sdk.model.PaymentInfoNewCard
import ru.rbs.mobile.payment.sdk.model.Theme
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.english
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.french
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.german
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.russian
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.spanish
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.ukrainian
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {

    private val launchLocale = Locale.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cardEasyButton.setOnClickListener { executeEasyCheckout() }
        cardCameraOffButton.setOnClickListener { executeCameraOffCheckout() }
        cardNfcOffButton.setOnClickListener { executeNfcOffCheckout() }
        cardDoneTextButton.setOnClickListener { executeDoneTextCheckout() }
        cardNewButton.setOnClickListener { executeCheckoutWithoutCards() }
        cardListNotRequiredCVCButton.setOnClickListener { executeCheckout(false) }
        darkThemeButton.setOnClickListener { executeThemeCheckout(true) }
        lightThemeButton.setOnClickListener { executeThemeCheckout(false) }
        cardListRequiredCVCButton.setOnClickListener { executeCheckout(true) }
        localeRuButton.setOnClickListener { executeLocaleCheckout(russian()) }
        localeEnButton.setOnClickListener { executeLocaleCheckout(english()) }
        localeDeButton.setOnClickListener { executeLocaleCheckout(german()) }
        localeFrButton.setOnClickListener { executeLocaleCheckout(french()) }
        localeEsButton.setOnClickListener { executeLocaleCheckout(spanish()) }
        localeUkButton.setOnClickListener { executeLocaleCheckout(ukrainian()) }

        GooglePayUtils.possiblyShowGooglePayButton(
            context = this,
            paymentsClient = GooglePayUtils.createPaymentsClient(
                context = this,
                environment = WalletConstants.ENVIRONMENT_TEST
            ),
            isReadyToPayJson = JSONObject(),
            callback = object : GooglePayUtils.GooglePayCheckCallback {
                override fun onNoGooglePlayServices() {
                    showGPayNotAvailableMessage()
                }

                override fun onNotReadyToRequest() {
                    showGPayNotAvailableMessage()
                }

                override fun onReadyToRequest() {
                    showGPayButtons()
                }
            }
        )
    }

    private fun showGPayNotAvailableMessage() {
        googlePayArea.visibility = GONE
        googlePayNotAvailableMessage.visibility = VISIBLE
    }

    private fun showGPayButtons() {
        googlePayArea.visibility = VISIBLE
        googlePayNotAvailableMessage.visibility = GONE
        listOf(
            googlePayButtonFirst,
            googlePayButtonSecond,
            googlePayButtonThird,
            googlePayButtonFourth
        ).forEach { button ->
            button.apply {
                setOnClickListener {
                    SDKPayment.cryptogram(this@MainActivity, createGooglePayConfig())
                }
            }
        }
        googlePayCryptogram.setOnClickListener {
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)?.let { clipboard ->
                clipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        "Google Pay Cryptogram",
                        googlePayCryptogram.text
                    )
                )
                log("Скопировано в буфер обмена")
            }
        }
    }

    private fun executeEasyCheckout() {
        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    private fun executeCameraOffCheckout() {
        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            .cameraScannerOptions(CameraScannerOptions.DISABLED)
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    private fun executeNfcOffCheckout() {
        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            .nfcScannerOptions(NfcScannerOptions.DISABLED)
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    private fun executeDoneTextCheckout() {
        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            .buttonText("Продлить подписку")
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    private fun executeCheckoutWithoutCards() {
        // Список связанных карт.
        val cards = emptySet<Card>()

        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            // Опционально, по умолчанию HIDE.
            .cardSaveOptions(CardSaveOptions.YES_BY_DEFAULT)
            // Опционально, по умолчанию HIDE.
            .holderInputOptions(HolderInputOptions.VISIBLE)
            // Опционально, локаль формы полаты, определяется автоматически.
            .locale(launchLocale)
            // Опционально, по умолчанию пустой список.
            .cards(cards)
            // Опционально, уникальный идентификатор платежа, генерируется автоматически.
            .uuid("27fb1ebf-895e-4b15-bfeb-6ecae378fe8e")
            // Опционально, время формирования платежа, устанавливается автоматически.
            .timestamp(System.currentTimeMillis())
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    private fun executeCheckout(bindingCVCRequired: Boolean) {
        // Список связанных карт.
        val cards = setOf(
            Card("492980xxxxxx7724", "aa199a55-cf16-41b2-ac9e-cddc731edd19", ExpiryDate(2025, 12)),
            Card("558620xxxxxx6614", "6617c0b1-9976-45d9-b659-364ecac099e2", ExpiryDate(2024, 6)),
            Card("415482xxxxxx0000", "3d2d320f-ca9a-4713-977c-c852accf8a7b", ExpiryDate(2019, 1)),
            Card("411790xxxxxx123456", "ceae68c1-cb02-4804-9526-6d6b2f1f2793")
        )

        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            // Опционально, по умолчанию локализованный перевод "Оплатить".
            .buttonText("Оплатить 200 Ꝑ")
            // Опционально, по умолчанию HIDE.
            .cardSaveOptions(CardSaveOptions.YES_BY_DEFAULT)
            // Опционально, по умолчанию HIDE.
            .holderInputOptions(HolderInputOptions.VISIBLE)
            // Опционально, по умолчанию true.
            .bindingCVCRequired(bindingCVCRequired)
            // Опционально, по умолчанию ENABLED.
            .cameraScannerOptions(CameraScannerOptions.ENABLED)
            // Опционально, по умолчанию ENABLED.
            .nfcScannerOptions(NfcScannerOptions.ENABLED)
            // Опционально, по умолчанию DEFAULT.
            .theme(Theme.DEFAULT)
            // Опционально, локаль формы полаты, определяется автоматически.
            .locale(launchLocale)
            // Опционально, по умолчанию пустой список.
            .cards(cards)
            // Опционально, уникальный идентификатор платежа, генерируется автоматически.
            .uuid("27fb1ebf-895e-4b15-bfeb-6ecae378fe8e")
            // Опционально, время формирования платежа, устанавливается автоматически.
            .timestamp(System.currentTimeMillis())
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    private fun executeThemeCheckout(isDark: Boolean) {
        // Список связанных карт.
        val cards = setOf(
            Card("492980xxxxxx7724", "aa199a55-cf16-41b2-ac9e-cddc731edd19", ExpiryDate(2025, 12)),
            Card("558620xxxxxx6614", "6617c0b1-9976-45d9-b659-364ecac099e2", ExpiryDate(2024, 6)),
            Card("415482xxxxxx0000", "3d2d320f-ca9a-4713-977c-c852accf8a7b", ExpiryDate(2019, 1)),
            Card("411790xxxxxx123456", "ceae68c1-cb02-4804-9526-6d6b2f1f2793")
        )

        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            // Опционально, по умолчанию локализованный перевод "Оплатить".
            .buttonText("Оплатить 200 Ꝑ")
            // Опционально, по умолчанию HIDE.
            .cardSaveOptions(CardSaveOptions.YES_BY_DEFAULT)
            // Опционально, по умолчанию HIDE.
            .holderInputOptions(HolderInputOptions.VISIBLE)
            // Опционально, по умолчанию true.
            .bindingCVCRequired(true)
            // Опционально, по умолчанию ENABLED.
            .cameraScannerOptions(CameraScannerOptions.ENABLED)
            // Опционально, по умолчанию SYSTEM.
            .theme(if (isDark) Theme.DARK else Theme.LIGHT)
            // Опционально, локаль формы полаты, определяется автоматически.
            .locale(launchLocale)
            // Опционально, по умолчанию пустой список.
            .cards(cards)
            // Опционально, уникальный идентификатор платежа, генерируется автоматически.
            .uuid("27fb1ebf-895e-4b15-bfeb-6ecae378fe8e")
            // Опционально, время формирования платежа, устанавливается автоматически.
            .timestamp(System.currentTimeMillis())
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    private fun executeLocaleCheckout(locale: Locale) {
        // Список связанных карт.
        val cards = setOf(
            Card("492980xxxxxx7724", "aa199a55-cf16-41b2-ac9e-cddc731edd19"),
            Card("5586200016956614", "ee199a55-cf16-41b2-ac9e-cc1c731edd19")
        )

        // Идентификатор заказа обязателен.
        val order = "00210bac-0ed1-474b-8ec2-5648cdfc4212"
        val paymentConfig = PaymentConfigBuilder(order)
            // Опционально, по умолчанию HIDE.
            .cardSaveOptions(CardSaveOptions.YES_BY_DEFAULT)
            // Опционально, по умолчанию HIDE.
            .holderInputOptions(HolderInputOptions.VISIBLE)
            // Опционально, локаль формы полаты, определяется автоматически.
            .locale(locale)
            // Опционально, по умолчанию пустой список.
            .cards(cards)
            // Опционально, уникальный идентификатор платежа, генерируется автоматически.
            .uuid("27fb1ebf-895e-4b15-bfeb-6ecae378fe8e")
            // Опционально, время формирования платежа, устанавливается автоматически.
            .timestamp(System.currentTimeMillis())
            .build()

        // Вызов экрана оплаты.
        SDKPayment.cryptogram(this, paymentConfig)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Обработка результата.
        SDKPayment.handleResult(requestCode, data, object : ResultCallback<PaymentData> {

            override fun onSuccess(result: PaymentData) {
                // Результат формирования криптограммы.
                when {
                    result.status.isSucceeded() -> {
                        val info = result.info
                        if (info is PaymentInfoNewCard) {
                            log("New card ${info.holder} ${info.saveCard}")
                        } else if (info is PaymentInfoBindCard) {
                            log("Saved card ${info.bindingId}")
                        } else if (info is PaymentInfoGooglePay) {
                            log("Google Pay ${info.order}")
                            googlePayCryptogram.text = result.cryptogram
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

    private fun createGooglePayConfig(): GooglePayPaymentConfig {
        val paymentData = GooglePayPaymentDataRequest.paymentDataRequestCreate {
            allowedPaymentMethods = AllowedPaymentMethods.allowedPaymentMethodsCreate {
                method {
                    type = GooglePayPaymentMethod.CARD
                    parameters = PaymentMethodParameters.paymentMethodParametersCreate {
                        allowedAuthMethods = mutableSetOf(
                            GooglePayAuthMethod.PAN_ONLY,
                            GooglePayAuthMethod.CRYPTOGRAM_3DS
                        )
                        allowedCardNetworks =
                            mutableSetOf(
                                GooglePayCardNetwork.AMEX,
                                GooglePayCardNetwork.DISCOVER,
                                GooglePayCardNetwork.INTERAC,
                                GooglePayCardNetwork.JCB,
                                GooglePayCardNetwork.MASTERCARD,
                                GooglePayCardNetwork.VISA
                            )
                    }
                    tokenizationSpecification =
                        TokenizationSpecification.tokenizationSpecificationCreate {
                            type = GoogleTokenizationSpecificationType.PAYMENT_GATEWAY
                            parameters =
                                TokenizationSpecificationParameters.tokenizationSpecificationParametersCreate {
                                    gateway = gatewayInput.text.toString()
                                    gatewayMerchantId = gatewayMerchantIdInput.text.toString()
                                }
                        }
                }
            }
            transactionInfo = TransactionInfo.transactionInfoCreate {
                totalPrice = BigDecimal.valueOf(amountInput.text.toString().toDoubleOrNull() ?: 0.0)
                totalPriceStatus = GooglePayTotalPriceStatus.FINAL
                countryCode = countryCodeInput.text.toString()
                currencyCode = currencyCodeInput.text.toString()
                checkoutOption = GooglePayCheckoutOption.COMPLETE_IMMEDIATE_PURCHASE
            }
            merchantInfo = MerchantInfo.merchantInfoCreate {
                merchantName = "Example Merchant"
                merchantId = merchantIdInput.text.toString()
            }
        }.toJson().toString()

        return GooglePayConfigBuilder(
            order = "eecbbe96-973e-422e-a220-e9fa8d6cb124",
            paymentData = PaymentDataRequest.fromJson(paymentData)
        ).testEnvironment(true)
            .build()
    }

    private fun log(message: String) {
        Log.d("LOG_TAG", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_theme_toggle) {
            toggleTheme()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun toggleTheme() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}
