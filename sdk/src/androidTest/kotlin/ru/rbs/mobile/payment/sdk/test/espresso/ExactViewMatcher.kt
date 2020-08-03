package ru.rbs.mobile.payment.sdk.test.espresso

import android.view.View
import org.hamcrest.BaseMatcher
import org.hamcrest.Description

class ExactViewMatcher private constructor(private val view: View) : BaseMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("exact view: $view")
    }

    override fun matches(item: Any?): Boolean = item == view

    companion object {
        fun exactView(view: View) =
            ExactViewMatcher(view)
    }
}
