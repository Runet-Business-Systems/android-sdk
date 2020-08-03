package ru.rbs.mobile.payment.sdk.utils

import io.kotest.matchers.shouldBe
import org.junit.Test

class StringExtensionsKtTest {

    @Test
    fun `pemKeyContent should remove spaces header and footer`() {
        val pem = """
        -----BEGIN PUBLIC KEY-----
        Content
        -----END PUBLIC KEY-----
        """.trimIndent()

        val content = pem.pemKeyContent()

        content shouldBe "Content"
    }
}
