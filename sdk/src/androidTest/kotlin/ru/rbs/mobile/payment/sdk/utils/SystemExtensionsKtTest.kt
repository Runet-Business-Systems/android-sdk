package ru.rbs.mobile.payment.sdk.utils

import org.junit.Test
import ru.rbs.mobile.payment.sdk.test.core.CoreTestActivity
import ru.rbs.mobile.payment.sdk.test.core.CoreUITest

class SystemExtensionsKtTest : CoreUITest<CoreTestActivity>(CoreTestActivity::class.java) {

    @Test
    fun shouldDisplayNfcEnableDialog() {
        activityTestRule.runOnUiThread {
            askToEnableNfc(activityTestRule.activity)
        }
        takeScreen()
    }
}
