package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.view.KeyEvent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.TypeTextAction
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
import ru.rbs.mobile.payment.sdk.utils.onDisplayError


class CardNumberEditTextTest : CoreUIViewTest<CardNumberEditText>() {

    override fun prepareView(context: Context): CardNumberEditText {
        return CardNumberEditText(context).apply {
            showError = true
            hint = getString(R.string.rbs_card_number)
        }
    }

    override fun wrapView(context: Context, view: CardNumberEditText): View {
        return BaseTextInputLayout(context).apply {
            addView(view)
            view onDisplayError { this.error = it }
        }
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeLatin() {
        onView(exactView(testedView)).perform(typeText("abcDess"))

        onView(exactView(testedView)).check(matches(withText("")))
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeCyrillic() {
        onView(exactView(testedView)).perform(replaceText("Ворота"))
        onView(exactView(testedView)).perform(typeText("1234"))

        onView(exactView(testedView)).check(matches(withText("1234")))
    }

    @Test
    @ConfigurationSingle
    fun shouldDisplayCorrectFormat() {
        onView(exactView(testedView)).perform(typeText("1234"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("1234")))

        onView(exactView(testedView)).perform(typeText("5"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("1234 5")))

        onView(exactView(testedView)).perform(typeText("6"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("1234 56")))

        onView(exactView(testedView)).perform(typeText("7"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("1234 567")))

        onView(exactView(testedView)).perform(typeText("8"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("1234 5678")))

        onView(exactView(testedView)).perform(typeText("9"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("1234 5678 9")))
    }

    @Test
    @ConfigurationSingle
    fun shouldAcceptMinNumberLength() {
        onView(exactView(testedView)).perform(typeText("5391119268214792"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("5391 1192 6821 4792")))
    }

    @Test
    @ConfigurationSingle
    fun shouldAcceptMaxNumberLength() {
        onView(exactView(testedView)).perform(typeText("3536360489308537405"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("3536 3604 8930 8537405")))
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeMoreThenMaxLength() {
        onView(exactView(testedView)).perform(typeText("35363604893085374051"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("3536 3604 8930 8537405")))
    }

    @Test
    @ConfigurationSingle
    fun shouldWorkBackspace() {
        onView(exactView(testedView)).perform(typeText("3536360489308537405"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("3536 3604 8930 8537405")))

        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("3536 3604 8930 853740")))

        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("3536 3604 8930 85374")))

        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("3536 3604 8930 8537")))

        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("3536 3604 8930")))
    }

    @Test
    @ConfigurationSingle
    fun shouldWorkEdit() {
        onView(exactView(testedView)).perform(typeText("35363604"))
        takeScreen()
        onView(exactView(testedView)).check(matches(withText("3536 3604")))

        activityTestRule.runOnUiThread {
            testedView.setSelection(3)
        }
        takeScreen()

        onView(exactView(testedView)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("3563 604")))
    }

    @Test
    @ConfigurationSingle
    fun shouldWorkInsert() {
        onView(exactView(testedView)).perform(typeText("35363604"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("3536 3604")))

        onView(exactView(testedView))
            .perform(
                GeneralClickAction(
                    Tap.SINGLE,
                    GeneralLocation.CENTER_LEFT,
                    Press.FINGER,
                    0,
                    0,
                    null
                ),
                TypeTextAction("9", false),
                TypeTextAction("7", false)
            )
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("9735 3636 04")))
    }

    @Test
    @ConfigurationSingle
    fun shouldCheckNumberByLunaAlgorithm() {
        onView(exactView(testedView)).perform(typeText("4485873525931601"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("4485 8735 2593 1601")))
    }
}
