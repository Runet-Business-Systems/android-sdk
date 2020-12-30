package ru.rbs.mobile.payment.sdk.core.test

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry

fun getString(resId: Int) = targetContext().resources.getString(resId)

fun targetContext(): Context = InstrumentationRegistry.getInstrumentation().targetContext
