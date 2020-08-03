package ru.rbs.mobile.payment.sdk.test.core

import android.content.Context
import android.view.View
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule


@LargeTest
abstract class CoreUIViewTest<VIEW : View>: CoreUITest() {

    @get:Rule
    val activityTestRule = ActivityTestRule<CoreMockActivity>(
        CoreMockActivity::class.java)

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
        val context = getActivity().window.decorView.rootView.context
        _testedView = prepareView(context)
        getActivity().runOnUiThread {
            _wrapperView = wrapView(context, _testedView)
            getActivity().setView(_wrapperView)
        }
    }

    abstract fun prepareView(context: Context): VIEW

    open fun wrapView(context: Context, view: VIEW): View = view
}
