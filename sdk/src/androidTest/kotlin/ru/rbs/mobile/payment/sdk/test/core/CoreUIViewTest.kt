package ru.rbs.mobile.payment.sdk.test.core

import android.content.Context
import android.view.View
import org.junit.Before

/**
 * Базовый класс создания тестов для View. Запускает Activity с расположенным на нем объектом
 * класса View предназначенного для тестирования.
 */
abstract class CoreUIViewTest<VIEW : View> :
    CoreUITest<CoreTestActivity>(CoreTestActivity::class.java) {

    protected lateinit var testedView: VIEW
        private set

    private lateinit var wrapperView: View

    @Before
    fun setup() {
        val context = getActivity().window.decorView.rootView.context
        testedView = prepareView(context)
        getActivity().runOnUiThread {
            wrapperView = wrapView(context, testedView)
            getActivity().setTestView(wrapperView)
        }
    }

    /**
     * Должен возвращать [VIEW], которое нужно расположить на экране Activity.
     *
     * @param context контекст Activity.
     * @return View для тестирования.
     */
    abstract fun prepareView(context: Context): VIEW

    /**
     * Переопределить при необходимости обернуть тестовое View.
     *
     * @param context контекст Activity.
     * @param view тестируемое View.
     * @return View содержащее в себе тестируемое View, либо саму View, если обертка не требуется.
     */
    open fun wrapView(context: Context, view: VIEW): View = view
}
