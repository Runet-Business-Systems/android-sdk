package ru.rbs.mobile.payment.sdk.ui

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import ru.rbs.mobile.payment.sdk.ui.helper.UIDelegate
import ru.rbs.mobile.payment.sdk.utils.setupDefaultResult

/**
 * Базовый класс для создания Activity.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val job = Job()
    protected val workScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val uiSetupDelegate: UIDelegate by lazy { UIDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        uiSetupDelegate.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setupDefaultResult()
    }

    override fun onResume() {
        super.onResume()
        uiSetupDelegate.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        uiSetupDelegate.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
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
}
