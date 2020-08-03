package ru.rbs.mobile.payment.sdk.test.core

import android.content.Context
import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.rules.RuleChain
import ru.rbs.mobile.payment.sdk.test.junit.LocaleRule
import ru.rbs.mobile.payment.sdk.ui.helper.Locales


@LargeTest
abstract class CoreUIViewLocalesTest<VIEW : View> : CoreUITest() {

    protected val activityTestRule = ActivityTestRule<CoreMockActivity>(
        CoreMockActivity::class.java, true, true
    )

    private val localeRule =
        LocaleRule(Locales.availableLocales())

    @get:Rule
    val ruleChain = RuleChain.outerRule(localeRule)
        .around(activityTestRule)
        .around(spoonRule)

    private lateinit var _testedView: VIEW
    private lateinit var _wrapperView: View

    protected val testedView
        get() = _testedView

    protected val wrapperView
        get() = _wrapperView

    override fun getActivity(): CoreMockActivity = activityTestRule.activity

    protected fun getTargetString(resId: Int) = getActivity().resources.getString(resId)

    @Before
    fun setup() {
        val activity = getActivity()
        _testedView = prepareView(activity)
        activity.runOnUiThread {
            _wrapperView = wrapView(activity, _testedView)
            activity.setView(_wrapperView)
        }
    }

    abstract fun prepareView(context: Context): VIEW

    open fun wrapView(context: Context, view: VIEW): View = view
}
