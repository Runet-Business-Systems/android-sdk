package ru.rbs.mobile.payment.sdk.test

import ru.rbs.mobile.payment.sdk.model.CameraScannerOptions
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions
import ru.rbs.mobile.payment.sdk.model.HolderInputOptions
import ru.rbs.mobile.payment.sdk.model.NfcScannerOptions
import ru.rbs.mobile.payment.sdk.model.PaymentConfig
import ru.rbs.mobile.payment.sdk.model.Theme
import java.util.*

object PaymentConfigTestProvider {

    fun defaultConfig() : PaymentConfig = PaymentConfig(
        order = UUID.randomUUID().toString(),
        uuid = UUID.randomUUID().toString(),
        timestamp = System.currentTimeMillis(),
        buttonText = null,
        locale = Locale.getDefault(),
        cards = emptySet(),
        cardSaveOptions = CardSaveOptions.HIDE,
        holderInputOptions = HolderInputOptions.HIDE,
        cameraScannerOptions = CameraScannerOptions.ENABLED,
        theme = Theme.SYSTEM,
        nfcScannerOptions = NfcScannerOptions.ENABLED,
        bindingCVCRequired = true
    )
}
