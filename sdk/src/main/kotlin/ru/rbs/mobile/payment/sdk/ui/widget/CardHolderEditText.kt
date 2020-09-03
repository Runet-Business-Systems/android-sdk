package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import ru.rbs.mobile.payment.sdk.ui.adapter.TextWatcherAdapter
import ru.rbs.mobile.payment.sdk.validation.CardHolderValidator

/**
 * UI элемент для ввода имени владельца карты.
 */
class CardHolderEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : BaseTextInputEditText(context, attrs, defStyleAttr) {

    private var cardHolderValidator: CardHolderValidator = CardHolderValidator(context)

    init {
        filters = arrayOf(
            object : InputFilter {
                private val regExp = "[a-zA-Z ]+".toRegex()

                override fun filter(
                    source: CharSequence,
                    start: Int,
                    end: Int,
                    dest: Spanned?,
                    dstart: Int,
                    dend: Int
                ): CharSequence? {
                    var keepOriginal = true
                    val sb = StringBuilder(end - start)
                    for (i in start until end) {
                        val c: Char = source[i]
                        if (regExp.matches(c.toString())) {
                            sb.append(c)
                        } else {
                            keepOriginal = false
                        }
                    }
                    return if (keepOriginal) null else {
                        if (source is Spanned) {
                            val sp = SpannableString(sb)
                            TextUtils.copySpansFrom(
                                source,
                                start,
                                sb.length,
                                null,
                                sp,
                                0
                            )
                            sp
                        } else {
                            sb
                        }
                    }
                }
            }, AllCaps()
        )
        inputType = InputType.TYPE_CLASS_TEXT
        maxLines = 1
        isSingleLine = true

        addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validate(s!!.toString())
            }
        })
        validate(text.toString())
    }

    private fun validate(value: String) {
        errorMessage = cardHolderValidator.validate(value).errorMessage
    }
}
