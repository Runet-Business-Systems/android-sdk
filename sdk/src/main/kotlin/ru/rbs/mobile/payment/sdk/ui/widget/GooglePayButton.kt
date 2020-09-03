package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ru.rbs.mobile.payment.sdk.R


/**
 * UI компонент для отображения кнопки оплаты Google Pay.
 */
class GooglePayButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : FrameLayout(context, attrs, defStyleAttr) {

    private var imageFormat: ImageFormat = ImageFormat.WITHOUT_TEXT
    private var backgroundFormat: BackgroundFormat = BackgroundFormat.SHADOW

    init {
        isClickable = true
        isFocusable = true
        setBackgroundColor(Color.TRANSPARENT)
        context.theme.obtainStyledAttributes(attrs, R.styleable.GooglePayButton, 0, 0).apply {
            try {
                val imageFormatValue =
                    getInteger(R.styleable.GooglePayButton_rbs_google_pay_button_image_format, 0)
                val backgroundFormatValue =
                    getInteger(
                        R.styleable.GooglePayButton_rbs_google_pay_button_background_format,
                        0
                    )
                imageFormat = ImageFormat.fromStyle(imageFormatValue)
                backgroundFormat = BackgroundFormat.fromStyle(backgroundFormatValue)
                updateView()
            } finally {
                recycle()
            }
        }
    }

    private fun updateView() {
        val layoutId = when (backgroundFormat) {
            BackgroundFormat.SHADOW -> when (imageFormat) {
                ImageFormat.WITH_TEXT -> R.layout.buy_with_googlepay_button
                ImageFormat.WITHOUT_TEXT -> R.layout.googlepay_button
            }
            BackgroundFormat.OUTLET -> when (imageFormat) {
                ImageFormat.WITH_TEXT -> R.layout.buy_with_googlepay_button_no_shadow
                ImageFormat.WITHOUT_TEXT -> R.layout.googlepay_button_no_shadow
            }
        }
        removeAllViews()
        LayoutInflater.from(context).inflate(layoutId, this)
    }


    /**
     * Установка формата изображения кнопки.
     *
     * @param format требуемый формат, один из [ImageFormat].
     */
    fun setImageFormat(format: ImageFormat) {
        this.imageFormat = format
        updateView()
    }

    /**
     * Установка формата заднего фона кнопки.
     *
     * @param format требуемый формат, один из [BackgroundFormat].
     */
    fun setBackgroundFormat(format: BackgroundFormat) {
        this.backgroundFormat = format
        updateView()
    }

    /**
     * Возможные варианты отображения изображения кнопки Google Pay.
     *
     * @param value значение из атрибута xml разметки.
     */
    enum class ImageFormat(val value: Int) {
        WITH_TEXT(0),
        WITHOUT_TEXT(1);

        companion object {

            /**
             * Возвращает фортам по значению из атрибута xml разметки.
             *
             * @param styleValue значение атрибута xml разметки.
             */
            fun fromStyle(styleValue: Int): ImageFormat =
                values().firstOrNull { it.value == styleValue } ?: WITHOUT_TEXT
        }
    }

    /**
     * Возможные варианты отображения фона кнопки Google Pay.
     *
     * @param value значение из атрибута xml разметки.
     */
    enum class BackgroundFormat(val value: Int) {
        SHADOW(0),
        OUTLET(1);

        companion object {

            /**
             * Возвращает фортам по значению из атрибута xml разметки.
             *
             * @param styleValue значение атрибута xml разметки.
             */
            fun fromStyle(styleValue: Int): BackgroundFormat =
                values().firstOrNull { it.value == styleValue } ?: SHADOW
        }
    }
}
