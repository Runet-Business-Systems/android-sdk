package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import org.junit.Test
import ru.rbs.mobile.payment.sdk.test.core.CoreUIViewTest
import ru.rbs.mobile.payment.sdk.ui.widget.GooglePayButton.BackgroundFormat.OUTLET
import ru.rbs.mobile.payment.sdk.ui.widget.GooglePayButton.BackgroundFormat.SHADOW
import ru.rbs.mobile.payment.sdk.ui.widget.GooglePayButton.ImageFormat.WITHOUT_TEXT
import ru.rbs.mobile.payment.sdk.ui.widget.GooglePayButton.ImageFormat.WITH_TEXT


class GooglePayButtonTest : CoreUIViewTest<GooglePayButton>() {

    override fun prepareView(context: Context): GooglePayButton {
        return GooglePayButton(context)
    }

    @Test
    fun shouldDisplayGooglePayButton() {
        activityTestRule.runOnUiThread {
            testedView.setBackgroundFormat(SHADOW)
        }

        activityTestRule.runOnUiThread {
            testedView.setImageFormat(WITH_TEXT)
        }
        takeScreen("shadow with text")

        activityTestRule.runOnUiThread {
            testedView.setImageFormat(WITHOUT_TEXT)
        }
        takeScreen("shadow without text")

        activityTestRule.runOnUiThread {
            testedView.setBackgroundFormat(OUTLET)
        }

        activityTestRule.runOnUiThread {
            testedView.setImageFormat(WITH_TEXT)
        }
        takeScreen("outlet with text")

        activityTestRule.runOnUiThread {
            testedView.setImageFormat(WITHOUT_TEXT)
        }
        takeScreen("outlet without text")
    }
}
