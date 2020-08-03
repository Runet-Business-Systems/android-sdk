package ru.rbs.mobile.payment.sample.java;

import android.app.Application;
import android.content.Context;

import ru.rbs.mobile.payment.sdk.SDKPayment;
import ru.rbs.mobile.payment.sdk.component.impl.CachedKeyProvider;
import ru.rbs.mobile.payment.sdk.component.impl.RemoteKeyProvider;

public class MarketApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKPayment.INSTANCE.init(this);
    }
}
