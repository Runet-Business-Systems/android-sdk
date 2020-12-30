package ru.rbs.mobile.payment.sdk.utils

import android.text.Editable
import ru.rbs.mobile.payment.sdk.ui.adapter.TextWatcherAdapter
import ru.rbs.mobile.payment.sdk.ui.widget.BaseTextInputEditText

/**
 * Установка обработчика для вывода сообщения ошибки.
 *
 * @param block обработчик вывода сообщения об ошибке.
 */
infix fun BaseTextInputEditText.onDisplayError(block: (s: String?) -> Unit) {
    errorMessageListener = object : BaseTextInputEditText.ErrorMessageListener {
        override fun displayError(message: String?) {
            block(message)
        }
    }
}

/**
 * Установка обработчика для обработки события корректного завершения заполнения поля.
 *
 * @param block обработчик корректного завершения заполнения поля.
 */
infix fun BaseTextInputEditText.onInputStatusChanged(block: () -> Unit) {
    inputStatusListener = object : BaseTextInputEditText.InputStatusListener {
        override fun inputCompleted() {
            block()
        }
    }
}

/**
 * Установка обработчика для обработки события изменения значения поля.
 *
 * @param block обработчик события изменения значения поля.
 */
infix fun BaseTextInputEditText.afterTextChanged(block: (s: String) -> Unit) {
    addTextChangedListener(object : TextWatcherAdapter() {
        override fun afterTextChanged(s: Editable?) {
            block(s.toString())
        }
    })
}
