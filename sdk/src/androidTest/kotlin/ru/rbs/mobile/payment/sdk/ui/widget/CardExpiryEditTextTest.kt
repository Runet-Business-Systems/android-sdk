package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.view.KeyEvent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.test.core.CoreUIViewTest
import ru.rbs.mobile.payment.sdk.test.espresso.ExactViewMatcher.Companion.exactView
import ru.rbs.mobile.payment.sdk.utils.onDisplayError

class CardExpiryEditTextTest : CoreUIViewTest<CardExpiryEditText>() {

    override fun prepareView(context: Context): CardExpiryEditText {
        return CardExpiryEditText(context).apply {
            showError = true
            hint = getTargetString(R.string.rbs_card_expiry_placeholder)
        }
    }

    override fun wrapView(context: Context, view: CardExpiryEditText): View {
        return BaseTextInputLayout(context).apply {
            addView(view)
            view onDisplayError { this.error = it }
        }
    }

    @Test
    fun shouldAllowInputOnlyCorrectMonthNumber() {
        onView(exactView(testedView)).perform(typeText("1220"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("12/20")))
    }

    @Test
    fun shouldNotAllowInputMoreThenMaxLength() {
        onView(exactView(testedView)).perform(typeText("12203"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("12/20")))
    }

    @Test
    fun shouldAllowInputOnlyCorrectYearNumber() {
        onView(exactView(testedView)).perform(typeText("9920"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("99/20")))
    }

    @Test
    fun shouldAllowInputOnlyDigits() {
        onView(exactView(testedView)).perform(typeText("a1b1 2N0"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("11/20")))
    }

    @Test
    fun shouldShowErrorForMaxYearLimit() {
        onView(exactView(testedView)).perform(typeText("0159"))
        takeScreen()
    }

    @Test
    fun shouldAppendDivider() {
        onView(exactView(testedView)).perform(typeText("0122"))
        takeScreen()

        onView(exactView(testedView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("01/2")))

        onView(exactView(testedView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("01/")))

        onView(exactView(testedView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("01")))

        onView(exactView(testedView)).perform(typeText("2"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("01/2")))
    }

    @Test
    fun shouldWorkBackspace() {
        onView(exactView(testedView)).perform(typeText("0122"))
        takeScreen()

        onView(exactView(testedView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("01/2")))

        onView(exactView(testedView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("01/")))

        onView(exactView(testedView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("01")))

        onView(exactView(testedView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("0")))

        onView(exactView(testedView)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("")))
    }
}
