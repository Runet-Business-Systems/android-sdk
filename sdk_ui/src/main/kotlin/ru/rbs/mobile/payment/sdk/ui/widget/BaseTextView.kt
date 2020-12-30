package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.util.AttributeSet

/**
 * Базовый компонент для реализации текстовых полей.
 */
open class BaseTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr)
