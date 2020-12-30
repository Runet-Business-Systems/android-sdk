package ru.rbs.mobile.payment.sample.kotlin.helpers

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Activity.log(message: String) {
    runOnUiThread {
        Log.d("LOG_TAG", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun launchGlobalScope(block: suspend CoroutineScope.() -> Unit): Job =
    GlobalScope.launch {
        block()
    }

fun launchMainGlobalScope(block: suspend CoroutineScope.() -> Unit): Job =
    GlobalScope.launch(Dispatchers.Main) {
        block()
    }


fun Activity.copyToClipboard(label: String, text: String) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)?.setPrimaryClip(
        ClipData.newPlainText(label, text)
    )
    log("Скопировано в буфер обмена")
}