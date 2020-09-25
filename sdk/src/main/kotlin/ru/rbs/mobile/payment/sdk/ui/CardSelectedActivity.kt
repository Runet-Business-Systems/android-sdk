package ru.rbs.mobile.payment.sdk.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_card_new.bankCardView
import kotlinx.android.synthetic.main.activity_card_new.cardCodeInput
import kotlinx.android.synthetic.main.activity_card_new.doneButton
import kotlinx.android.synthetic.main.activity_card_new.toolbar
import kotlinx.android.synthetic.main.activity_card_selected.cardCodeInputLayout
import kotlinx.android.synthetic.main.list_item_card_saved.view.cardExpiry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rbs.mobile.payment.sdk.Constants
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.component.CryptogramProcessor
import ru.rbs.mobile.payment.sdk.model.Card
import ru.rbs.mobile.payment.sdk.model.CardBindingIdIdentifier
import ru.rbs.mobile.payment.sdk.model.CardInfo
import ru.rbs.mobile.payment.sdk.model.PaymentConfig
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.model.PaymentDataStatus
import ru.rbs.mobile.payment.sdk.model.PaymentInfoBindCard
import ru.rbs.mobile.payment.sdk.ui.helper.CardResolver
import ru.rbs.mobile.payment.sdk.utils.onDisplayError

/**
 * Экран выбранной карты из списка связок карт.
 */
class CardSelectedActivity : BaseActivity() {

    private var cryptogramProcessor: CryptogramProcessor = SDKPayment.cryptogramProcessor
    private val config: PaymentConfig by lazy {
        intent.getParcelableExtra<PaymentConfig>(Constants.INTENT_EXTRA_CONFIG) as PaymentConfig
    }
    private val card: Card by lazy {
        intent.getParcelableExtra<Card>(Constants.INTENT_EXTRA_CARD) as Card
    }
    private val cardResolver: CardResolver by lazy {
        CardResolver(
            bankCardView = bankCardView,
            cardInfoProvider = SDKPayment.sdkConfig.cardInfoProvider
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_selected)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.rbs_title_payment)
        }
        cardResolver.resolve(card.pan)
        config.buttonText?.let { text ->
            doneButton.text = text
        }
        config.bindingCVCRequired.let { cvcRequired ->
            cardCodeInputLayout.visibility = if (cvcRequired) VISIBLE else INVISIBLE
        }
        bankCardView.apply {
            setNumber(card.pan)
            enableHolderName(false)
            if (card.expiryDate != null) {
                cardExpiry.setExpiry(card.expiryDate!!)
                cardExpiry.visibility = View.VISIBLE
            } else {
                cardExpiry.visibility = View.INVISIBLE
                cardExpiry.setExpiry("")
            }
        }
        cardCodeInput onDisplayError { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        doneButton.setOnClickListener {
            onDone()
        }
    }

    private fun onDone() {
        if (!config.bindingCVCRequired) {
            preparePaymentData()
        } else if (cardCodeInput.errorMessage != null) {
            cardCodeInput.showError = true
        } else {
            preparePaymentData()
        }
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
                        identifier = CardBindingIdIdentifier(card.bindingId),
                        cvv = cardCodeInput.text.toString().toIntOrNull()
                    )
                )
                finishWithResult(
                    PaymentData(
                        status = PaymentDataStatus.SUCCEEDED,
                        cryptogram = cryptogram,
                        info = PaymentInfoBindCard(
                            order = config.order,
                            bindingId = card.bindingId
                        )
                    )
                )
            } catch (e: Exception) {
                finishWithError(exception = e)
            }
        }
    }

    companion object {

        /**
         * Подготавливает [Intent] для запуска экрана оплаты выбранной картой.
         *
         * @param context для подготовки intent.
         * @param config конфигурация оплаты.
         */
        fun prepareIntent(
            context: Context,
            config: PaymentConfig,
            card: Card
        ): Intent = Intent(context, CardSelectedActivity::class.java).apply {
            putExtra(Constants.INTENT_EXTRA_CONFIG, config)
            putExtra(Constants.INTENT_EXTRA_CARD, card)
        }
    }
}
