package ru.rbs.mobile.payment.sdk.test.core

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry

fun getString(resId: Int) =
    InstrumentationRegistry.getInstrumentation().targetContext.resources.getString(resId)

fun targetContext(): Context = InstrumentationRegistry.getInstrumentation().targetContext
