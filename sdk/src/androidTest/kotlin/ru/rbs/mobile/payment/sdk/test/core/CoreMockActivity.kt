package ru.rbs.mobile.payment.sdk.test.core

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import ru.rbs.mobile.payment.sdk.test.R
import ru.rbs.mobile.payment.sdk.ui.BaseActivity

class CoreMockActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock)
    }

    fun setView(view: View) {
        findViewById<ViewGroup>(R.id.mockContainer).apply {
            removeAllViews()
            addView(view)
        }
    }
}
