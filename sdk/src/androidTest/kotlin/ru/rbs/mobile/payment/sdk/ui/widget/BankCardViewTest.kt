package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.test.SleepEmulator.sleep
import ru.rbs.mobile.payment.sdk.test.core.CoreUIViewTest

class BankCardViewTest : CoreUIViewTest<BankCardView>() {

    override fun prepareView(context: Context): BankCardView {
        return BankCardView(context).apply {
            setupUnknownBrand()
        }
    }

    @Test
    fun shouldDisplayNumberMask() {
        takeScreen()

        activityTestRule.runOnUiThread {
            testedView.setNumber("123")
        }
        takeScreen()
        onView(withId(R.id.cardNumber)).check(matches(withText("123• •••• •••• ••••  ")))

        activityTestRule.runOnUiThread {
            testedView.setNumber("123456")
        }
        takeScreen()
        onView(withId(R.id.cardNumber)).check(matches(withText("1234 56•• •••• ••••  ")))

        activityTestRule.runOnUiThread {
            testedView.setNumber("1234567890123456")
        }
        takeScreen()
        onView(withId(R.id.cardNumber)).check(matches(withText("1234 5678 9012 3456  ")))

        activityTestRule.runOnUiThread {
            testedView.setNumber("1234567890123456789")
        }
        takeScreen()
        onView(withId(R.id.cardNumber)).check(matches(withText("1234 5678 9012 3456789")))
    }


    @Test
    fun shouldDisplayExpireMask() {
        takeScreen()
        onView(withId(R.id.cardExpiry))
            .check(matches(withText("•• / ••")))

        activityTestRule.runOnUiThread {
            testedView.setExpiry("12")
        }
        takeScreen()
        onView(withId(R.id.cardExpiry)).check(matches(withText("12 / ••")))

        activityTestRule.runOnUiThread {
            testedView.setExpiry("12/25")
        }
        takeScreen()
        onView(withId(R.id.cardExpiry)).check(matches(withText("12 / 25")))

        activityTestRule.runOnUiThread {
            testedView.setExpiry("55555")
        }
        takeScreen()
        onView(withId(R.id.cardExpiry)).check(matches(withText("55 / 55")))

        activityTestRule.runOnUiThread {
            testedView.setExpiry("123456789")
        }
        takeScreen()
        sleep()
        onView(withId(R.id.cardExpiry)).check(matches(withText("12 / 34")))
    }
}
