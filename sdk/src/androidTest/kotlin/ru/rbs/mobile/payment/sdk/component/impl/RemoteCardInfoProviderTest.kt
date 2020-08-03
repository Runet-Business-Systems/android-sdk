package ru.rbs.mobile.payment.sdk.component.impl

import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.component.CardInfoProvider
import ru.rbs.mobile.payment.sdk.component.CardInfoProviderException

class RemoteCardInfoProviderTest {

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
    fun shouldReturnCardInfo() = runBlockingTest {
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
        assertEquals("${urlBin}logo/invert/364b8b2f-64f1-4268-b1df-9b19575c68e1/1.svg", info.logoInvert)
        assertEquals("visa", info.paymentSystem)
        assertEquals("SUCCESS", info.status)
    }

    @Test(expected = CardInfoProviderException::class)
    fun shouldReturnCardInfoProviderExceptionForIncorrectResponseBody() = runBlockingTest {
        server.enqueue(
            MockResponse().setBody(
                "Incorrect body response"
            )
        )

        cardInfoProvider.resolve("12345")
    }

    @Test(expected = CardInfoProviderException::class)
    fun shouldReturnCardInfoProviderExceptionForErrorCodeResponse() = runBlockingTest {
        server.enqueue(
            MockResponse().setHttp2ErrorCode(500)
        )

        cardInfoProvider.resolve("12345")
    }
}
