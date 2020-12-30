package ru.rbs.mobile.payment.sdk.test.core

import android.Manifest
import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule.grant
import com.squareup.spoon.SpoonRule
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.rules.TestName
import ru.rbs.mobile.payment.sdk.test.junit.ConfigurationRule

/**
 * Базовый класс для создания UI тестов. Класс содержит всю необходимую функциональность для
 * выполнения UI тестов.
 *
 * Предоставления права на запись файлов во внешнее хранилище устройства.
 * Получение снимков экрана при выполнении теста.
 * Конфигурация среды (выбор темы и локали) теста.
 *
 * @param activityClass класс Activity для запуска теста.
 * @param initialTouchMode true если Activity должно быть переведено в "touch mode" при старте.
 * @param launchActivity true если необходимо запустить Activity единожды.
 */
@LargeTest
open class CoreUITest<ACTIVITY : Activity>(
    activityClass: Class<ACTIVITY>,
    initialTouchMode: Boolean = true,
    launchActivity: Boolean = true
) {

    private val permissionRule = grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val configurationRule = ConfigurationRule()

    private val spoonRule = SpoonRule()

    private val testName = TestName()

    protected val activityTestRule =
        ActivityTestRule<ACTIVITY>(activityClass, initialTouchMode, launchActivity)

    protected fun getActivity(): ACTIVITY = activityTestRule.activity

    protected fun getString(resId: Int) = getActivity().resources.getString(resId)

    @get:Rule
    val ruleChain: RuleChain = RuleChain.outerRule(configurationRule)
        .around(activityTestRule)
        .around(spoonRule)
        .around(testName)
        .around(permissionRule)

    /**
     * Делает снимок текущего состояния Activity и прикрепляет его к отчету тестирования.
     *
     * @param shotName название снимка.
     * @param closeSoftKeyboard true если необходимо скрывать программную клавиатуру перед снимком.
     */
    fun takeScreen(shotName: String = "", closeSoftKeyboard: Boolean = true) {
        if (closeSoftKeyboard) {
            onView(isRoot()).perform(closeSoftKeyboard())
        }
        val methodName = testName.methodName
        val localeName = configurationRule.currentLocale.toString()
        val themeName = configurationRule.currentTheme.toString()
        val tag = listOf(methodName, shotName, localeName, themeName).filter {
            it.isNotBlank()
        }.joinToString(separator = "_", transform = { it.replace(tagRegex, "") })
        spoonRule.screenshot(getActivity(), tag)
    }

    companion object {

        /**
         * Выражение для проверки допустимых значений в названии файла снимка экрана.
         */
        private val tagRegex = "[^a-zA-Z0-9_-]".toRegex()
    }
}
