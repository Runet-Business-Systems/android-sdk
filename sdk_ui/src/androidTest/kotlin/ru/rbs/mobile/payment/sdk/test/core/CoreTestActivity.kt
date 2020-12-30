package ru.rbs.mobile.payment.sdk.test.core

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import ru.rbs.mobile.payment.sdk.test.R
import ru.rbs.mobile.payment.sdk.ui.BaseActivity

/**
 * Activity используемое для тестирования компонентов UI компонентов, таких как
 * View, Dialog, Fragment и прочие.
 */
class CoreTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    /**
     * Устанавливает переданное [view] в разметку экрана.
     *
     * @param view View для тестирования.
     */
    fun setTestView(view: View) {
        findViewById<ViewGroup>(R.id.testContainer).apply {
            removeAllViews()
            addView(view)
        }
    }
}
