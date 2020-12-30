package ru.rbs.mobile.payment.sdk.ui.adapter

import android.text.Editable
import android.text.TextWatcher

internal abstract class TextWatcherAdapter : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        // переопределить при необходимости
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // переопределить при необходимости
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // переопределить при необходимости
    }
}
