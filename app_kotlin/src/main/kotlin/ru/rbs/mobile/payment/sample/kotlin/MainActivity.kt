package ru.rbs.mobile.payment.sample.kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.rbs.mobile.payment.sdk.PaymentConfigBuilder
import ru.rbs.mobile.payment.sdk.ResultCallback
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.model.CameraScannerOptions
import ru.rbs.mobile.payment.sdk.model.Card
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions
import ru.rbs.mobile.payment.sdk.model.ExpiryDate
import ru.rbs.mobile.payment.sdk.model.HolderInputOptions
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.model.PaymentInfoBindCard
import ru.rbs.mobile.payment.sdk.model.PaymentInfoNewCard
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.english
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.french
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.german
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.russian
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.spanish
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.ukrainian
import java.util.*

class MainActivity : AppCompatActivity() {

    private val launchLocale = Locale.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cardEasyButton.setOnClickListener { executeEasyCheckout() }
        cardCameraOffButton.setOnClickListener { executeCameraOffCheckout() }
        cardDoneTextButton.setOnClickListener { executeDoneTextCheckout() }
        cardNewButton.setOnClickListener { executeCheckoutWithoutCards() }
        cardListNotRequiredCVCButton.setOnClickListener { executeCheckout(false) }
        cardListRequiredCVCButton.setOnClickListener { executeCheckout(true) }
        localeRuButton.setOnClickListener { executeLocaleCheckout(russian()) }
        localeEnButton.setOnClickListener { executeLocaleCheckout(english()) }
        localeDeButton.setOnClickListener { executeLocaleCheckout(german()) }
        localeFrButton.setOnClickListener { executeLocaleCheckout(french()) }
        localeEsButton.setOnClickListener { executeLocaleCheckout(spanish()) }
        localeUkButton.setOnClickListener { executeLocaleCheckout(ukrainian()) }
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
                        }
                        log("$result")
                    }
                    result.status.isCanceled() -> {
                        log("canceled")
                    }
                }
            }

            override fun onFail(e: Exception) {
                // Возникла ошибка.
                log(e.toString())
            }
        })
    }

    private fun log(message: String) {
        Log.d("LOG_TAG", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
