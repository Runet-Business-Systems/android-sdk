package ru.rbs.mobile.payment.sdk.utils

import android.app.Activity
import android.content.Intent
import ru.rbs.mobile.payment.sdk.Constants
import ru.rbs.mobile.payment.sdk.SDKException
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.model.PaymentDataStatus

/**
 * Устанавливает результат выполнения работы [Activity] по умолчанию в значение
 * [PaymentDataStatus.CANCELED].
 */
fun Activity.setupDefaultResult() {
    val resultIntent = Intent().apply {
        putExtra(
            Constants.INTENT_EXTRA_RESULT, PaymentData(
                status = PaymentDataStatus.CANCELED,
                cryptogram = ""
            )
        )
    }
    setResult(Activity.RESULT_OK, resultIntent)
}

/**
 * Завершает работу [Activity] на котором был вызван данный метод с передачей в качестве результата
 * работы [Activity] значения [payment].
 *
 * @param payment - результат работы [Activity]
 */
fun Activity.finishWithResult(payment: PaymentData) {
    val resultIntent = Intent().apply {
        putExtra(Constants.INTENT_EXTRA_RESULT, payment)
    }
    setResult(Activity.RESULT_OK, resultIntent)
    finish()
}

/**
 * Завершает работу [Activity] на котором был вызван данный метод с передачей в качестве результата
 * работы [Activity] значения [exception].
 *
 * @param exception - результат работы [Activity]
 */
fun Activity.finishWithError(exception: Exception) {
    val resultException = if (exception !is SDKException) {
        SDKException(
            cause = exception
        )
    } else {
        exception
    }
    val resultIntent = Intent().apply {
        putExtra(Constants.INTENT_EXTRA_ERROR, resultException)
    }
    setResult(Activity.RESULT_OK, resultIntent)
    finish()
}
