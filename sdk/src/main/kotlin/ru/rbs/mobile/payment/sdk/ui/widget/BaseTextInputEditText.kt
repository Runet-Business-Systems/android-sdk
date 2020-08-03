package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

/**
 * Базовый компонент для реализации полей ввода.
 */
open class BaseTextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    /**
     * Слушатель для вывода сообщения об ошибке.
     */
    var errorMessageListener: ErrorMessageListener? = null

    /**
     * Слушатель для определения состояния заполнения поля.
     */
    var inputStatusListener: InputStatusListener? = null

    /**
     * Текущее сообщение об ошибке ввода данных.
     */
    var errorMessage: String? = null
        protected set(message) {
            errorMessageListener?.displayError(message.takeIf { showError })
            field = message
            if (message == null) {
                inputStatusListener?.inputCompleted()
            }
        }

    /**
     * Настройка необходимости отображать ошибку ввода данных.
     */
    var showError: Boolean = false
        set(needShow) {
            errorMessage?.let { message ->
                errorMessageListener?.displayError(message.takeIf { needShow })
            }
            field = needShow
        }

    /**
     * Интерфейс для отображения сообщения ошибки.
     */
    interface ErrorMessageListener {

        /**
         * Вызывается для вывода текущего сообщения об ошибке.
         *
         * @param message сообщение об ошибке если она есть, null в противном случае.
         */
        fun displayError(message: String?)
    }

    /**
     * Интерфейс для определения состояния заполнения поля.
     */
    interface InputStatusListener {

        /**
         * Вызывается после введения последнего символа, при полном и корректном заполнении поля.
         */
        fun inputCompleted()
    }
}
