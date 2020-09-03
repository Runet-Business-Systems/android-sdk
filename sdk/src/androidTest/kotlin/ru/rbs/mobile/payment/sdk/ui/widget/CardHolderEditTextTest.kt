package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.view.KeyEvent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.test.core.CoreUIViewTest
import ru.rbs.mobile.payment.sdk.test.espresso.ExactViewMatcher.Companion.exactView
import ru.rbs.mobile.payment.sdk.test.junit.ConfigurationSingle

class CardHolderEditTextTest : CoreUIViewTest<CardHolderEditText>() {

    override fun prepareView(context: Context): CardHolderEditText {
        return CardHolderEditText(context).apply {
            showError = true
            hint = getString(R.string.rbs_card_holder_placeholder)
        }
    }

    override fun wrapView(context: Context, view: CardHolderEditText): View {
        return BaseTextInputLayout(context).apply {
            addView(view)
        }
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeDigitsAtTheStart() {
        onView(exactView(testedView)).perform(typeText("123abcDe"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("ABCDE")))
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeDigitsAtTheMiddle() {
        onView(exactView(testedView)).perform(typeText("Ho23me"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("HOME")))
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeDigitsAtTheEnd() {
        onView(exactView(testedView)).perform(typeText("Home23"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("HOME")))
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeCyrillicAtTheEnd() {
        onView(exactView(testedView)).perform(typeText("JUMP"))
        takeScreen()
        onView(exactView(testedView)).perform(replaceText("вода"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("")))
    }

    @Test
    @ConfigurationSingle
    fun shouldAllowTypeLatinWithSpace() {
        onView(exactView(testedView)).perform(typeText("John Doe"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("JOHN DOE")))
    }

    @Test
    @ConfigurationSingle
    fun shouldWorkBackspace() {
        onView(exactView(testedView)).perform(typeText("John Doe"))
        takeScreen()
        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("JOHN D")))
    }
}
