package ru.rbs.mobile.payment.sdk.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import ru.rbs.mobile.payment.sdk.Constants.INTENT_EXTRA_RESULT
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.model.PaymentDataStatus
import ru.rbs.mobile.payment.sdk.ui.helper.LocalizationActivityDelegate

/**
 * Базовый класс для создания Activity.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val job = Job()
    protected val workScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val localizationDelegate: LocalizationActivityDelegate by lazy {
        LocalizationActivityDelegate(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.onCreate()
        super.onCreate(savedInstanceState)
        val resultIntent = Intent().apply {
            putExtra(
                INTENT_EXTRA_RESULT, PaymentData(
                    status = PaymentDataStatus.CANCELED,
                    cryptogram = ""
                )
            )
        }
        setResult(Activity.RESULT_OK, resultIntent)
    }

    override fun onResume() {
        super.onResume()
        localizationDelegate.onResume()
    }

    override fun onDestroy() {
        workScope.cancel()
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase))
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    protected fun finishWithResult(payment: PaymentData) {
        val resultIntent = Intent().apply {
            putExtra(INTENT_EXTRA_RESULT, payment)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
