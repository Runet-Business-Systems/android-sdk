package ru.rbs.mobile.payment.sdk.ui

import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasFocus
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.SDKConfigBuilder
import ru.rbs.mobile.payment.sdk.SDKPayment
import ru.rbs.mobile.payment.sdk.component.CryptogramProcessor
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions
import ru.rbs.mobile.payment.sdk.model.HolderInputOptions
import ru.rbs.mobile.payment.sdk.test.PaymentConfigTestProvider.defaultConfig
import ru.rbs.mobile.payment.sdk.test.SleepEmulator.sleep
import ru.rbs.mobile.payment.sdk.test.core.CoreUITest
import ru.rbs.mobile.payment.sdk.test.core.targetContext
import ru.rbs.mobile.payment.sdk.test.espresso.TextInputLayoutErrorTextMatcher.Companion.hasTextInputLayoutHintText
import ru.rbs.mobile.payment.sdk.test.junit.LocaleRule
import ru.rbs.mobile.payment.sdk.ui.helper.Locales

class CardNewActivityTest : CoreUITest() {

    private val localeRule =
        LocaleRule(Locales.availableLocales())

    private val mockCryptogramProcessor: CryptogramProcessor = mockk()

    private val activityTestRule =
        object : ActivityTestRule<CardNewActivity>(CardNewActivity::class.java, true, false) {

            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                SDKPayment.innerCryptogramProcessor = mockCryptogramProcessor
                SDKPayment.innerSdkConfig = SDKConfigBuilder(targetContext()).build()
            }
        }


    override fun getActivity(): AppCompatActivity = activityTestRule.activity

    private fun getString(resId: Int) = getActivity().resources.getString(resId)

    @get:Rule
    val ruleChain = RuleChain.outerRule(localeRule)
        .around(activityTestRule)
        .around(spoonRule)

    @Test
    fun shouldRunWithCorrectLocale() {
        val config = defaultConfig()
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()
    }

    @Test
    fun shouldScrollWithSoftwareKeyboard() {
        val config = defaultConfig().copy(
            holderInputOptions = HolderInputOptions.VISIBLE,
            cardSaveOptions = CardSaveOptions.NO_BY_DEFAULT
        )
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.cardHolderInputLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.checkSaveCard)).check(matches(isDisplayed()))
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
        onView(withId(R.id.cardCodeInput)).perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.rootLayout)).perform(swipeUp())
        onView(withId(R.id.doneButton)).perform(click())
        onView(withId(R.id.rootLayout)).perform(swipeUp())
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
        onView(withId(R.id.cardCodeInput)).perform(typeText("123"))
        onView(withId(R.id.rootLayout)).perform(swipeUp())
        sleep()
        onView(withId(R.id.doneButton)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldAutoJumpToNextInput() {
        val config = defaultConfig().copy(
            holderInputOptions = HolderInputOptions.VISIBLE,
            cardSaveOptions = CardSaveOptions.NO_BY_DEFAULT
        )
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.cardNumberInput)).perform(typeText("4594929153014323"))
        onView(withId(R.id.cardExpiryInput)).check(matches(hasFocus()))
        onView(withId(R.id.cardExpiryInput)).perform(typeText("12/27"))
        onView(withId(R.id.cardCodeInput)).check(matches(hasFocus()))
        onView(withId(R.id.cardCodeInput)).perform(typeText("123"))
        onView(withId(R.id.cardHolderInput)).check(matches(hasFocus()))
        onView(withId(R.id.cardHolderInput)).perform(typeText("JOHN"))
    }

    @Test
    fun shouldRunWithConfiguredButtonText() {
        val config = defaultConfig().copy(buttonText = "Configured Text")
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.doneButton))
            .check(matches(withText("Configured Text")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldRunWithConfiguredSaveCardHide() {
        val config = defaultConfig().copy(cardSaveOptions = CardSaveOptions.HIDE)
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.checkSaveCard)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldRunWithConfiguredSaveCardYesByDefault() {
        val config = defaultConfig().copy(cardSaveOptions = CardSaveOptions.YES_BY_DEFAULT)
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.checkSaveCard))
            .check(matches(isDisplayed()))
            .check(matches(isChecked()))
    }

    @Test
    fun shouldRunWithConfiguredHolderInputHide() {
        val config = defaultConfig().copy(holderInputOptions = HolderInputOptions.HIDE)
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.cardHolderInput)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldRunWithConfiguredHolderInputHideAndCardSaveHideAndConfiguredButton() {
        val config = defaultConfig().copy(
            holderInputOptions = HolderInputOptions.HIDE,
            cardSaveOptions = CardSaveOptions.HIDE,
            buttonText = "Fast payment"
        )
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.cardHolderInput)).check(matches(not(isDisplayed())))
        onView(withId(R.id.checkSaveCard)).check(matches(not(isDisplayed())))
        onView(withId(R.id.doneButton))
            .check(matches(withText("Fast payment")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldRunWithConfiguredHolderInputVisible() {
        val config = defaultConfig().copy(holderInputOptions = HolderInputOptions.VISIBLE)
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.cardHolderInput)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldRunWithConfiguredSaveCardNoByDefault() {
        val config = defaultConfig().copy(
            cardSaveOptions = CardSaveOptions.NO_BY_DEFAULT
        )
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        takeScreen()

        onView(withId(R.id.checkSaveCard))
            .check(matches(isDisplayed()))
            .check(matches(isNotChecked()))
    }

    @Test
    fun shouldProceedValidData() {
        val config = defaultConfig().copy(
            holderInputOptions = HolderInputOptions.VISIBLE
        )
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        coEvery {
            mockCryptogramProcessor.create(any(), any(), any(), any())
        } returns ""
        onView(withId(R.id.cardNumberInput)).perform(
            typeText("5586200016956614"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        sleep()
        takeScreen()

        onView(withId(R.id.cardExpiryInput)).perform(
            typeText("1225"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        takeScreen()

        onView(withId(R.id.cardCodeInput)).perform(
            typeText("123"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        takeScreen()

        onView(withId(R.id.cardHolderInput)).perform(
            typeText("KONSTANTINOPOLSKY"),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
        takeScreen()

        onView(withId(R.id.doneButton)).perform(click())

        coVerify {
            mockCryptogramProcessor.create(any(), any(), any(), any())
        }
    }

    @Test
    fun shouldDisplayCardLengthError() {
        val config = defaultConfig()
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        onView(withId(R.id.cardNumberInput)).perform(
            typeText("55862000"),
            closeSoftKeyboard()
        )
        takeScreen()

        onView(withId(R.id.doneButton)).perform(click())
        takeScreen()

        onView(withId(R.id.cardNumberInputLayout)).check(
            matches(
                hasTextInputLayoutHintText(
                    getString(R.string.rbs_card_incorrect_length)
                )
            )
        )
    }

    @Test
    fun shouldDisplayCodeError() {
        val config = defaultConfig()
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        onView(withId(R.id.cardCodeInput)).perform(
            typeText("12")
        )
        takeScreen()

        onView(withId(R.id.doneButton)).perform(click())
        takeScreen()

        onView(withId(R.id.cardCodeInputLayout)).check(
            matches(
                hasTextInputLayoutHintText(
                    getString(R.string.rbs_card_incorrect_cvc)
                )
            )
        )
    }

    @Test
    fun shouldDisplayExpireError() {
        val config = defaultConfig()
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        onView(withId(R.id.cardExpiryInput)).perform(
            typeText("55")
        )
        takeScreen()

        onView(withId(R.id.doneButton)).perform(click())
        takeScreen()

        onView(withId(R.id.cardExpiryInputLayout)).check(
            matches(
                hasTextInputLayoutHintText(
                    getString(R.string.rbs_card_incorrect_expiry)
                )
            )
        )
    }

    @Test
    fun shouldDisplayAllErrors() {
        val config = defaultConfig()
        val launchIntent = CardNewActivity.prepareIntent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
        activityTestRule.launchActivity(launchIntent)
        onView(withId(R.id.cardNumberInput)).perform(
            typeText("55862000"),
            closeSoftKeyboard()
        )
        takeScreen()
        onView(withId(R.id.cardCodeInput)).perform(
            typeText("12"),
            closeSoftKeyboard()
        )
        takeScreen()
        onView(withId(R.id.cardExpiryInput)).perform(
            typeText("55"),
            closeSoftKeyboard()
        )
        takeScreen()

        onView(withId(R.id.doneButton)).perform(click())
        takeScreen()

        onView(withId(R.id.cardNumberInputLayout)).check(
            matches(
                hasTextInputLayoutHintText(
                    getString(R.string.rbs_card_incorrect_length)
                )
            )
        )
        onView(withId(R.id.cardCodeInputLayout)).check(
            matches(
                hasTextInputLayoutHintText(
                    getString(R.string.rbs_card_incorrect_cvc)
                )
            )
        )
        onView(withId(R.id.cardExpiryInputLayout)).check(
            matches(
                hasTextInputLayoutHintText(
                    getString(R.string.rbs_card_incorrect_expiry)
                )
            )
        )
    }
}
