package ru.rbs.mobile.payment.sdk.test.espresso

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class TextInputLayoutErrorTextMatcher private constructor(private val errorMessage: String) :
    TypeSafeMatcher<View>() {

    override fun matchesSafely(view: View?): Boolean {
        if (view !is TextInputLayout) {
            return false
        }
        return errorMessage == view.error?.toString() ?: ""
    }

    override fun describeTo(description: Description?) {
        description?.appendText("errorMessage: $errorMessage")
    }

    companion object {
        fun hasTextInputLayoutHintText(errorMessage: String) =
            TextInputLayoutErrorTextMatcher(
                errorMessage
            )
    }
}
