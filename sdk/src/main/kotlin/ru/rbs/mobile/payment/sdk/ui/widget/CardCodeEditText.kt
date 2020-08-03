package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.Spanned
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import ru.rbs.mobile.payment.sdk.ui.adapter.TextWatcherAdapter
import ru.rbs.mobile.payment.sdk.utils.digitsOnly
import ru.rbs.mobile.payment.sdk.validation.CardCodeValidator

/**
 * UI элемент для ввода секретного кода карты.
 */
class CardCodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : BaseTextInputEditText(context, attrs, defStyleAttr) {

    private var cardCodeValidator: CardCodeValidator = CardCodeValidator(context)

    init {
        inputType = InputType.TYPE_CLASS_NUMBER
        transformationMethod = PasswordTransformationMethod()
        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                transformationMethod = PasswordTransformationMethod()
            }
        }
        filters = arrayOf(object : InputFilter {
            override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence {
                if (source == "" || source.toString().matches("[0-9]+".toRegex())) {
                    return source
                }
                return ""
            }
        }, LengthFilter(CODE_MAX_LENGTH))

        addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validate(s!!.toString().digitsOnly())
            }
        })
        validate(text.toString().digitsOnly())
    }

    private fun validate(value: String) {
        errorMessage = cardCodeValidator.validate(value).errorMessage
    }

    companion object {
        private const val CODE_MAX_LENGTH = 3
    }
}
