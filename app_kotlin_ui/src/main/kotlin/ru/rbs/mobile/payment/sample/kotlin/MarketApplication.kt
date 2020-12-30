@file:Suppress("UndocumentedPublicClass")
package ru.rbs.mobile.payment.sample.kotlin

import android.app.Application
import ru.rbs.mobile.payment.sdk.SDKPayment

@Suppress( "UndocumentedPublicClass")
class MarketApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SDKPayment.init(this)
    }
}
