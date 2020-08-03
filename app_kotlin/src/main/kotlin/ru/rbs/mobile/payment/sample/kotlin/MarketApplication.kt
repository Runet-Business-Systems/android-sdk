package ru.rbs.mobile.payment.sample.kotlin

import android.app.Application
import ru.rbs.mobile.payment.sdk.SDKConfigBuilder
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.component.impl.RemoteCardInfoProvider
import ru.rbs.mobile.payment.sdk.component.impl.RemoteKeyProvider

class MarketApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SDKPayment.init(this)
    }
}