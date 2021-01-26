package ru.rbs.mobile.payment.sdk.component.impl

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import ru.rbs.mobile.payment.sdk.component.CardInfoProvider
import ru.rbs.mobile.payment.sdk.component.CardInfoProviderException

class RemoteCardInfoProviderTest {

    @get:Rule
    val permissionRule: TestRule =
        GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private lateinit var cardInfoProvider: CardInfoProvider
    private val server: MockWebServer = MockWebServer()
    private lateinit var urlBin: String

    @Before
    fun setUp() {
        server.start()
        val url = server.url("/").toString()
        urlBin = "${url}bins/"
        cardInfoProvider = RemoteCardInfoProvider(
            url = url,
            urlBin = urlBin
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun shouldReturnCardInfo() = runBlocking {
        server.enqueue(
            MockResponse().setBody(
                """
                {
                    "name": "Райффайзенбанк",
                    "nameEn": "Raiffeisenbank bank",
                    "backgroundColor": "#000000",
                    "backgroundGradient": [
                        "#eeeeee",
                        "#efe6a2"
                    ],
                    "supportedInvertTheme": false,
                    "backgroundLightness": true,
                    "country": "ru",
                    "defaultLanguage": "ru",
                    "textColor": "#000",
                    "url": "https://www.raiffeisen.ru/",
                    "logo": "logo/main/364b8b2f-64f1-4268-b1df-9b19575c68e1/1.svg",
                    "logoInvert": "logo/invert/364b8b2f-64f1-4268-b1df-9b19575c68e1/1.svg",
                    "logoMini": "logo/mini/364b8b2f-64f1-4268-b1df-9b19575c68e1/1.svg",
                    "paymentSystem": "visa",
                    "cobrand": null,
                    "status": "SUCCESS"
                }
            """.trimIndent()
            )
        )

        val info = cardInfoProvider.resolve("446916")
        assertEquals("#000000", info.backgroundColor)
        assertEquals("#eeeeee", info.backgroundGradient[0])
        assertEquals("#efe6a2", info.backgroundGradient[1])
        assertEquals(true, info.backgroundLightness)
        assertEquals("#000", info.textColor)
        assertEquals("${urlBin}logo/main/364b8b2f-64f1-4268-b1df-9b19575c68e1/1.svg", info.logo)
        assertEquals(
            "${urlBin}logo/invert/364b8b2f-64f1-4268-b1df-9b19575c68e1/1.svg",
            info.logoInvert
        )
        assertEquals("visa", info.paymentSystem)
        assertEquals("SUCCESS", info.status)
        Unit
    }

    @Test(expected = CardInfoProviderException::class)
    fun shouldReturnCardInfoProviderExceptionForIncorrectResponseBody() = runBlocking {
        server.enqueue(
            MockResponse().setBody(
                "Incorrect body response"
            )
        )

        cardInfoProvider.resolve("12345")
        Unit
    }

    @Test(expected = CardInfoProviderException::class)
    fun shouldReturnCardInfoProviderExceptionForErrorCodeResponse() = runBlocking {
        server.enqueue(
            MockResponse().setHttp2ErrorCode(500)
        )

        cardInfoProvider.resolve("12345")
        Unit
    }
}
