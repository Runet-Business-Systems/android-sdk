package ru.rbs.mobile.payment.sample.kotlin.core

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.rbs.mobile.payment.sdk.core.SDKCore
import ru.rbs.mobile.payment.sdk.core.TokenResult
import ru.rbs.mobile.payment.sdk.core.model.BindingParams
import ru.rbs.mobile.payment.sdk.core.model.CardParams
import ru.rbs.mobile.payment.sdk.core.validation.BaseValidator
import ru.rbs.mobile.payment.sdk.core.validation.CardCodeValidator
import ru.rbs.mobile.payment.sdk.core.validation.CardExpiryValidator
import ru.rbs.mobile.payment.sdk.core.validation.CardHolderValidator
import ru.rbs.mobile.payment.sdk.core.validation.CardNumberValidator
import ru.rbs.mobile.payment.sdk.core.validation.OrderNumberValidator

class MainActivity : AppCompatActivity() {
    // инициализация валидаторов для полей ввода информации о карте
    private val cardNumberValidator by lazy { CardNumberValidator(this) }
    private val cardExpiryValidator by lazy { CardExpiryValidator(this) }
    private val cardCodeValidator by lazy { CardCodeValidator(this) }
    private val cardHolderValidator by lazy { CardHolderValidator(this) }
    private val orderNumberValidator by lazy { OrderNumberValidator(this) }
    private val sdkCore by lazy { SDKCore(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // установка валидаторов на поля ввода информации о карте
        cardNumberInput.setupValidator(cardNumberValidator)
        cardExpiryInput.setupValidator(cardExpiryValidator)
        cardCodeInput.setupValidator(cardCodeValidator)
        cardHolderInput.setupValidator(cardHolderValidator)
        mdOrderInput.setupValidator(orderNumberValidator)

        generateWithCard.setOnClickListener {
            // создание объекта и инициализация полей для новой карты
            val params = CardParams(
                mdOrder = mdOrderInput.text.toString(),
                pan = cardNumberInput.text.toString(),
                cvc = cardCodeInput.text.toString(),
                expiryMMYY = cardExpiryInput.text.toString(),
                cardHolder = cardHolderInput.text.toString(),
                pubKey = pubKeyInput.text.toString()
            )
            // вызов метода для получения криптограммы для новой карты
            proceedResult(sdkCore.generateWithCard(params))
        }

        generateWithBinding.setOnClickListener {
            // создание объекта и инициализация полей для привязанной карты
            val params = BindingParams(
                mdOrder = mdOrderInput.text.toString(),
                bindingID = bindingIdInput.text.toString(),
                cvc = "123",
                pubKey = pubKeyInput.text.toString()
            )
            // вызов метода для получения криптограммы для привязанной карты
            proceedResult(sdkCore.generateWithBinding(params))
        }
    }

    private fun proceedResult(tokenResult: TokenResult) {
        val message = with(tokenResult) {
            if (errors.isEmpty()) token else errors.map { "${it.key} ${it.value}" }
                .joinToString()
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun EditText.setupValidator(validator: BaseValidator<String>) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validator.validate(s.toString()).let { result ->
                    if (result.isValid) {
                        this@setupValidator.setError(null, null)
                    } else {
                        this@setupValidator.setError(
                            "[${result.errorCode}] ${result.errorMessage}",
                            null
                        )
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }


}