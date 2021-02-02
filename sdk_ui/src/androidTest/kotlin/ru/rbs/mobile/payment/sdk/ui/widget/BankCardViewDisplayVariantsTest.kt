package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.test.core.CoreUIViewTest
import ru.rbs.mobile.payment.sdk.test.junit.ConfigurationLocales

class BankCardViewDisplayVariantsTest : CoreUIViewTest<BankCardView>() {

    private lateinit var server: MockWebServer

    override fun prepareView(context: Context): BankCardView {
        return BankCardView(context)
    }

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun MockWebServer.enqueueFile(fileName: String): String {
        val fileContent = this::class.java.getResource("/$fileName")!!.readText()
        server.enqueue(
            MockResponse().setBody(fileContent)
        )
        return server.url("/").toString()
    }

    @Suppress("LongMethod", "ComplexMethod")
    @ConfigurationLocales(["en"])
    @Test
    fun shouldCardDisplayVariantsLocalBank() {
        takeScreen("Empty")
        val url = server.enqueueFile("bank-invert.svg")
        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl(url)
                setBackground("#63d01c", "#01b27a")
                setHolderName("Mabel Fergie")
                setTextColor("#fff")
                setExpiry("03/20")
                setNumber("42764414")
                setPaymentSystem("visa", true)
            }
        }
        takeScreen("Bank")
    }

    @Suppress("LongMethod", "ComplexMethod")
    @ConfigurationLocales(["en"])
    @Test
    fun shouldCardDisplayVariantsLocalTinkoff() {
        takeScreen("Empty")
        val url = server.enqueueFile("tinkoff-invert.svg")
        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl(url)
                setBackground("#444", "#222")
                setHolderName("MARK WATNEY")
                setTextColor("#fff")
                setExpiry("03/25")
                setNumber("55369138")
                setPaymentSystem("mastercard", true)
            }
        }
        takeScreen("Tinkoff Bank")
    }

    @Suppress("LongMethod", "ComplexMethod")
    @ConfigurationLocales(["en"])
    @Test
    fun shouldCardDisplayVariantsLocalGazprom() {
        takeScreen("Empty")
        val url = server.enqueueFile("gazprom-invert.svg")
        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl(url)
                setBackground("#044b98", "#02356c")
                setHolderName("RICHARD HENDRICKS")
                setTextColor("#fff")
                setExpiry("09/20")
                setNumber("67645463")
                setPaymentSystem("mastercard", true)
            }
        }
        takeScreen("Gazprom Bank")
    }

    @Suppress("LongMethod", "ComplexMethod")
    @ConfigurationLocales(["en"])
    @Test
    fun shouldCardDisplayVariantsLocalQiwi() {
        takeScreen("Empty")
        val url = server.enqueueFile("qiwi-invert.svg")
        activityTestRule.runOnUiThread {
            testedView.apply {
                setBankLogoUrl(url)
                setBackground("#ff9f1c", "#fe7a1d")
                setHolderName("JOHN DOE")
                setTextColor("#fff")
                setExpiry("08/22")
                setNumber("53213003")
                setPaymentSystem("mastercard", true)
            }
        }
        takeScreen("Qiwi Bank")
    }

}
