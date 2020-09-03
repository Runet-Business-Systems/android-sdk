package ru.rbs.mobile.payment.sdk.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import ru.rbs.mobile.payment.sdk.Constants.INTENT_EXTRA_ERROR
import ru.rbs.mobile.payment.sdk.Constants.INTENT_EXTRA_RESULT
import ru.rbs.mobile.payment.sdk.SDKException
import ru.rbs.mobile.payment.sdk.model.PaymentData
import ru.rbs.mobile.payment.sdk.model.PaymentDataStatus
import ru.rbs.mobile.payment.sdk.ui.helper.UIDelegate

/**
 * Базовый класс для создания Activity.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val job = Job()
    protected val workScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val uiSetupDelegate: UIDelegate by lazy { UIDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        uiSetupDelegate.onCreate()
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
        uiSetupDelegate.onResume()
    }

    override fun onDestroy() {
        workScope.cancel()
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(uiSetupDelegate.attachBaseContext(newBase))
    }

    override fun getApplicationContext(): Context {
        return uiSetupDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return uiSetupDelegate.getResources(super.getResources())
    }

    // https://stackoverflow.com/questions/55265834/change-locale-not-work-after-migrate-to-androidx/58004553#58004553
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
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

    protected fun finishWithError(exception: Exception) {
        val resultException = if (exception !is SDKException) {
            SDKException(
                cause = exception
            )
        } else {
            exception
        }
        val resultIntent = Intent().apply {
            putExtra(INTENT_EXTRA_ERROR, resultException)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
