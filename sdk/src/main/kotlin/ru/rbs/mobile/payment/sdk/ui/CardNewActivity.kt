package ru.rbs.mobile.payment.sdk.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.devnied.emvnfccard.exception.CommunicationException
import io.card.payment.CardIOActivity
import io.card.payment.CardIOActivity.EXTRA_HIDE_CARDIO_LOGO
import io.card.payment.CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE
import io.card.payment.CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME
import io.card.payment.CardIOActivity.EXTRA_REQUIRE_CVV
import io.card.payment.CardIOActivity.EXTRA_REQUIRE_EXPIRY
import io.card.payment.CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE
import io.card.payment.CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION
import io.card.payment.CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.activity_card_new.bankCardView
import kotlinx.android.synthetic.main.activity_card_new.cardCodeInput
import kotlinx.android.synthetic.main.activity_card_new.cardCodeInputLayout
import kotlinx.android.synthetic.main.activity_card_new.cardExpiryInput
import kotlinx.android.synthetic.main.activity_card_new.cardExpiryInputLayout
import kotlinx.android.synthetic.main.activity_card_new.cardHolderInput
import kotlinx.android.synthetic.main.activity_card_new.cardHolderInputLayout
import kotlinx.android.synthetic.main.activity_card_new.cardNumberInput
import kotlinx.android.synthetic.main.activity_card_new.cardNumberInputLayout
import kotlinx.android.synthetic.main.activity_card_new.checkSaveCard
import kotlinx.android.synthetic.main.activity_card_new.doneButton
import kotlinx.android.synthetic.main.activity_card_new.toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rbs.mobile.payment.sdk.Constants.INTENT_EXTRA_CONFIG
import ru.rbs.mobile.payment.sdk.Constants.REQUEST_CODE_SCAN_CARD
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.component.CryptogramProcessor
import ru.rbs.mobile.payment.sdk.model.CameraScannerOptions
import ru.rbs.mobile.payment.sdk.model.CardInfo
import ru.rbs.mobile.payment.sdk.model.CardPanIdentifier
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions
import ru.rbs.mobile.payment.sdk.model.HolderInputOptions
import ru.rbs.mobile.payment.sdk.model.NfcScannerOptions
import ru.rbs.mobile.payment.sdk.model.PaymentConfig
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.model.PaymentDataStatus
import ru.rbs.mobile.payment.sdk.model.PaymentInfoNewCard
import ru.rbs.mobile.payment.sdk.nfc.NFCReadDelegate
import ru.rbs.mobile.payment.sdk.ui.helper.CardResolver
import ru.rbs.mobile.payment.sdk.ui.helper.LocalizationSetting
import ru.rbs.mobile.payment.sdk.ui.widget.BaseTextInputEditText
import ru.rbs.mobile.payment.sdk.utils.addRightButtons
import ru.rbs.mobile.payment.sdk.utils.afterTextChanged
import ru.rbs.mobile.payment.sdk.utils.askToEnableNfc
import ru.rbs.mobile.payment.sdk.utils.deviceHasCamera
import ru.rbs.mobile.payment.sdk.utils.deviceHasNFC
import ru.rbs.mobile.payment.sdk.utils.digitsOnly
import ru.rbs.mobile.payment.sdk.utils.onDisplayError
import ru.rbs.mobile.payment.sdk.utils.onInputStatusChanged
import ru.rbs.mobile.payment.sdk.utils.toExpDate
import ru.rbs.mobile.payment.sdk.utils.toStringExpDate
import java.util.*


/**
 * Экран новой карты.
 */
@Suppress("TooManyFunctions")
class CardNewActivity : BaseActivity() {

    private var cryptogramProcessor: CryptogramProcessor = SDKPayment.cryptogramProcessor
    private val cardResolver: CardResolver by lazy {
        CardResolver(
            bankCardView = bankCardView,
            cardInfoProvider = SDKPayment.sdkConfig.cardInfoProvider
        )
    }
    private val config: PaymentConfig by lazy {
        intent.getParcelableExtra<PaymentConfig>(INTENT_EXTRA_CONFIG) as PaymentConfig
    }

    private var nfcReader: NFCReadDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_new)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.rbs_title_payment)
        }
        configure(config)
    }

    private val jumpToNextInput: () -> Unit = {
        val firstNotCompletedField =
            activeInputFields().firstOrNull {
                it.errorMessage != null || it.text.toString().isEmpty()
            }
        if (firstNotCompletedField != null) {
            firstNotCompletedField.requestFocus()
        } else {
            hideKeyboard()
        }
    }

    private val nfcCardListener = object : NFCReadDelegate.NFCCardListener {
        override fun onCardReadSuccess(number: String, expiryDate: Date?) {
            cardNumberInput.setText(number)
            expiryDate?.let { date ->
                cardExpiryInput.setText(date.toStringExpDate())
            } ?: cardExpiryInput.setText("")
        }

        override fun onCardReadError(e: Exception) {
            val errorMessage = when (e) {
                is CommunicationException -> R.string.rbs_nfc_do_not_move_card
                else -> R.string.rbs_nfc_read_error
            }
            Toast.makeText(
                this@CardNewActivity,
                errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun configure(config: PaymentConfig) {
        bankCardView.setupUnknownBrand()
        cardNumberInput onInputStatusChanged jumpToNextInput
        cardExpiryInput onInputStatusChanged jumpToNextInput
        cardCodeInput onInputStatusChanged jumpToNextInput
        cardNumberInput onDisplayError { cardNumberInputLayout.error = it }
        cardHolderInput onDisplayError { cardHolderInputLayout.error = it }
        cardExpiryInput onDisplayError { cardExpiryInputLayout.error = it }
        cardCodeInput onDisplayError { cardCodeInputLayout.error = it }
        cardHolderInput afterTextChanged { holder -> bankCardView.setHolderName(holder) }
        cardExpiryInput afterTextChanged { expiry -> bankCardView.setExpiry(expiry) }
        cardNumberInput afterTextChanged { number ->
            bankCardView.setNumber(number)
            cardResolver.resolve(
                number = number,
                withDelay = true
            )
        }
        cardNumberInput
        doneButton.setOnClickListener { onDone() }
        config.buttonText?.let { text -> doneButton.text = text }
        when (config.cardSaveOptions) {
            CardSaveOptions.HIDE -> {
                checkSaveCard.visibility = GONE
            }
            CardSaveOptions.YES_BY_DEFAULT -> {
                checkSaveCard.visibility = VISIBLE
                checkSaveCard.isChecked = true
            }
            CardSaveOptions.NO_BY_DEFAULT -> {
                checkSaveCard.visibility = VISIBLE
                checkSaveCard.isChecked = false
            }
        }
        when (config.holderInputOptions) {
            HolderInputOptions.HIDE -> {
                cardHolderInputLayout.visibility = GONE
                bankCardView.enableHolderName(false)
            }
            HolderInputOptions.VISIBLE -> {
                cardHolderInputLayout.visibility = VISIBLE
                bankCardView.enableHolderName(true)
            }
        }
        val buttons: MutableList<Pair<Int, () -> Unit>> = mutableListOf()
        if (config.nfcScannerOptions == NfcScannerOptions.ENABLED && deviceHasNFC(this)) {
            nfcReader = NFCReadDelegate(NfcAdapter.getDefaultAdapter(applicationContext)).apply {
                nfcCardListener = this@CardNewActivity.nfcCardListener
            }
            buttons.add(R.drawable.ic_nfc to { showHintNFC() })
        } else {
            nfcReader = null
        }
        if (config.cameraScannerOptions == CameraScannerOptions.ENABLED && deviceHasCamera(this)) {
            buttons.add(R.drawable.ic_card to { startScanner() })
        }
        cardNumberInput.addRightButtons(buttons)
    }

    override fun onResume() {
        super.onResume()
        nfcReader?.onResume(this, javaClass)
    }

    override fun onPause() {
        super.onPause()
        nfcReader?.onPause(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        nfcReader?.onNewIntent(intent)
    }

    private fun onDone() {
        val fields = activeInputFields()
        if (fields.all { it.errorMessage == null }) {
            preparePaymentData()
        } else {
            fields.filter { it.errorMessage != null }.forEach { it.showError = true }
        }
    }

    private fun activeInputFields(): MutableList<BaseTextInputEditText> {
        val fields = mutableListOf(cardNumberInput, cardExpiryInput, cardCodeInput)
        if (config.holderInputOptions == HolderInputOptions.VISIBLE) {
            fields.add(cardHolderInput)
        }
        return fields
    }

    @Suppress("TooGenericExceptionCaught")
    private fun preparePaymentData() {
        workScope.launch(Dispatchers.Main) {
            try {
                val cryptogram = cryptogramProcessor.create(
                    order = config.order,
                    uuid = config.uuid,
                    timestamp = config.timestamp,
                    cardInfo = CardInfo(
                        identifier = CardPanIdentifier(
                            cardNumberInput.text.toString().digitsOnly()
                        ),
                        expDate = cardExpiryInput.text.toString().toExpDate(),
                        cvv = cardCodeInput.text.toString().toInt()
                    )
                )
                finishWithResult(
                    PaymentData(
                        status = PaymentDataStatus.SUCCEEDED,
                        cryptogram = cryptogram,
                        info = PaymentInfoNewCard(
                            order = config.order,
                            saveCard = checkSaveCard.isChecked,
                            holder = cardHolderInput.text.toString()
                        )
                    )
                )
            } catch (e: Exception) {
                finishWithError(exception = e)
            }
        }
    }

    private fun startScanner() {
        val scanIntent = Intent(this, CardIOActivity::class.java).apply {
            putExtra(EXTRA_REQUIRE_EXPIRY, false)
            putExtra(EXTRA_REQUIRE_CARDHOLDER_NAME, false)
            putExtra(EXTRA_REQUIRE_CVV, false)
            putExtra(EXTRA_REQUIRE_POSTAL_CODE, false)
            putExtra(EXTRA_HIDE_CARDIO_LOGO, true)
            putExtra(EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
            putExtra(EXTRA_SUPPRESS_CONFIRMATION, true)
            putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
            putExtra(
                CardIOActivity.EXTRA_SCAN_INSTRUCTIONS,
                getString(R.string.rbs_card_scan_message)
            )
            LocalizationSetting.getLanguage()?.toLanguageTag().let { languageTag ->
                putExtra(EXTRA_LANGUAGE_OR_LOCALE, languageTag)
            }
        }
        startActivityForResult(scanIntent, REQUEST_CODE_SCAN_CARD)
    }

    private fun showHintNFC() {
        if (nfcReader!!.isEnabled()) {
            Toast.makeText(this, R.string.rbs_nfc_hold_card_to_phone, Toast.LENGTH_SHORT).show()
        } else {
            askToEnableNfc(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SCAN_CARD) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
                    ?.let { result ->
                        cardNumberInput.setText(result.cardNumber)
                    }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {

        /**
         * Подготавливает [Intent] для запуска экрана оплаты новой картой.
         *
         * @param context для подготовки intent.
         * @param config конфигурация оплаты.
         */
        fun prepareIntent(
            context: Context,
            config: PaymentConfig
        ): Intent = Intent(context, CardNewActivity::class.java).apply {
            putExtra(INTENT_EXTRA_CONFIG, config)
        }
    }
}
