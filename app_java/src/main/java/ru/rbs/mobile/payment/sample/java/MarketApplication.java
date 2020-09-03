package ru.rbs.mobile.payment.sample.java;

import android.app.Application;

import ru.rbs.mobile.payment.sdk.SDKPayment;

public class MarketApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKPayment.INSTANCE.init(this, null);
    }
}
