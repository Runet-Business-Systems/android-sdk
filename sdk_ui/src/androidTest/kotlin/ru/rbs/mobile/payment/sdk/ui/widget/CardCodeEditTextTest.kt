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
import ru.rbs.mobile.payment.sdk.utils.onDisplayError

class CardCodeEditTextTest : CoreUIViewTest<CardCodeEditText>() {

    override fun prepareView(context: Context): CardCodeEditText {
        return CardCodeEditText(context).apply {
            showError = true
            hint = getString(R.string.rbs_code)
        }
    }

    override fun wrapView(context: Context, view: CardCodeEditText): View {
        return BaseTextInputLayout(context).apply {
            addView(view)
            view onDisplayError { this.error = it }
        }
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeLatin() {
        onView(exactView(testedView)).perform(typeText("abcDe"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("")))
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeCyrillic() {
        onView(exactView(testedView)).perform(replaceText("абГд"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("")))
    }

    @Test
    @ConfigurationSingle
    fun shouldNotAllowTypeMoreThenMaxLength() {
        onView(exactView(testedView)).perform(typeText("1225"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("122")))
    }

    @Test
    @ConfigurationSingle
    fun shouldMaskInput() {
        onView(exactView(testedView)).perform(typeText("012"))
        takeScreen()

        onView(exactView(testedView)).check(matches(withText("012")))
    }

    @Test
    @ConfigurationSingle
    fun shouldWorkBackspace() {
        onView(exactView(testedView)).perform(typeText("012"))
        takeScreen()
        onView(exactView(testedView))
            .perform(pressKey(KeyEvent.KEYCODE_DEL)).apply { takeScreen() }
            .perform(pressKey(KeyEvent.KEYCODE_DEL)).apply { takeScreen() }
            .perform(pressKey(KeyEvent.KEYCODE_DEL)).apply { takeScreen() }

        onView(exactView(testedView)).check(matches(withText("")))
    }
}
