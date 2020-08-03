package ru.rbs.mobile.payment.sdk.test.core

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.rules.TestName
import ru.rbs.mobile.payment.sdk.test.espresso.ExactViewMatcher.Companion.exactView
import ru.rbs.mobile.payment.sdk.ui.helper.LocalizationSetting

abstract class CoreUITest {

    @get:Rule
    val testName = TestName()

    @get:Rule
    val spoonRule = com.squareup.spoon.SpoonRule()

    @get:Rule
    val permissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    var hideKeyboardBeforeTakeScreen = true

    fun takeScreen(name: String = "") {
        if (hideKeyboardBeforeTakeScreen) {
            onView(exactView(getActivity().window.decorView.rootView)).perform(closeSoftKeyboard())
        }
        val localeTag = LocalizationSetting.getLanguage()?.toLanguageTag() ?: ""
        val tag = listOf(testName.methodName, name, localeTag).filter {
            it.isNotBlank()
        }.joinToString(separator = "_", transform = { it.replace(tagRegex, "") })
        spoonRule.screenshot(getActivity(), tag)
    }

    abstract fun getActivity(): AppCompatActivity

    companion object {

        private val tagRegex = "[^a-zA-Z0-9_-]".toRegex()
    }
}
