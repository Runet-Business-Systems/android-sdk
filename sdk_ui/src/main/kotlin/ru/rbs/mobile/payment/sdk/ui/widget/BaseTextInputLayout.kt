package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout

/**
 * Базовый компонент для реализации разметки для полей ввода.
 */
open class BaseTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.textInputStyle
) : TextInputLayout(context, attrs, defStyleAttr)
