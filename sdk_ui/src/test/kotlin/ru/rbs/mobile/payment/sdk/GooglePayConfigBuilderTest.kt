package ru.rbs.mobile.payment.sdk

import com.google.android.gms.wallet.PaymentDataRequest
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.comparables.shouldNotBeEqualComparingTo
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.beEmpty
import org.junit.Test
import ru.rbs.mobile.payment.sdk.model.Theme
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.english
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.french
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.german
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.russian
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.spanish
import ru.rbs.mobile.payment.sdk.ui.helper.Locales.ukrainian

class GooglePayConfigBuilderTest {

    @Test
    fun `should return theme`() {
        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).theme(Theme.DARK)
            .build()
            .theme shouldBe Theme.DARK


        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).theme(Theme.LIGHT)
            .build()
            .theme shouldBe Theme.LIGHT

        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).theme(Theme.SYSTEM)
            .build()
            .theme shouldBe Theme.SYSTEM

        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).theme(Theme.DEFAULT)
            .build()
            .theme shouldBe Theme.DEFAULT
    }

    @Test
    fun `should return locale`() {
        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).locale(english())
            .build()
            .locale.language shouldBe "en"

        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).locale(german())
            .build()
            .locale.language shouldBe "de"

        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).locale(french())
            .build()
            .locale.language shouldBe "fr"

        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).locale(spanish())
            .build()
            .locale.language shouldBe "es"

        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).locale(russian())
            .build()
            .locale.language shouldBe "ru"

        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).locale(ukrainian())
            .build()
            .locale.language shouldBe "uk"
    }

    @Test
    fun `should return generated uuid value by default`() {
        val order = "2545f609-490b-4c8d-a3fb-be1f8d30fa77"

        val firstUUID = GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).build().uuid

        val secondUUID = GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).build().uuid

        firstUUID shouldNot beEmpty()
        secondUUID shouldNot beEmpty()
        firstUUID shouldNotBeEqualComparingTo secondUUID
    }

    @Test
    fun `should return defined uuid`() {
        val expectedUuid = "62d0bb9e-111b-4c28-a79e-e7fb8d1791eb"

        val actualUuid = GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).uuid(expectedUuid)
            .build()
            .uuid

        actualUuid shouldBe expectedUuid
    }

    @Test
    fun `should return same generated value of uuid for the same builder`() {
        val builder = GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        )

        val firstUUID = builder
            .build()
            .uuid
        val secondUUID = builder
            .build()
            .uuid

        firstUUID shouldNot beEmpty()
        secondUUID shouldNot beEmpty()
        firstUUID shouldBe secondUUID
    }

    @Test
    fun `should return current timestamp by default`() {
        val order = "039e5501-d39b-47c1-a0e5-bd1d3ae917ad"

        val beforeExecute = System.currentTimeMillis()
        Thread.sleep(100)
        val firstTimestamp = GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).build().timestamp
        Thread.sleep(100)
        val secondTimestamp = GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).build().timestamp
        Thread.sleep(100)
        val afterExecute = System.currentTimeMillis()

        firstTimestamp shouldNotBe 0L
        secondTimestamp shouldNotBe 0L
        firstTimestamp shouldNotBeEqualComparingTo secondTimestamp
        firstTimestamp shouldBeGreaterThan beforeExecute
        secondTimestamp shouldBeGreaterThan beforeExecute
        firstTimestamp shouldBeLessThan afterExecute
        secondTimestamp shouldBeLessThan afterExecute
    }

    @Test
    fun `should return defined timestamp`() {
        val expectedTimestamp = 1593791597L

        val actualTimestamp = GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).timestamp(expectedTimestamp)
            .build()
            .timestamp

        actualTimestamp shouldBe expectedTimestamp
    }

    @Test
    fun `should return same value of timestamp for the same builder`() {
        val builder = GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        )

        val firstTimestamp = builder
            .build()
            .timestamp
        val secondTimestamp = builder
            .build()
            .timestamp

        firstTimestamp shouldNotBe 0L
        secondTimestamp shouldNotBe 0L
        firstTimestamp shouldBe secondTimestamp
    }

    @Test
    fun `should return testEnvironment`() {
        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        )
            .testEnvironment(true)
            .build()
            .testEnvironment shouldBe true

        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        )
            .testEnvironment(false)
            .build()
            .testEnvironment shouldBe false
    }

    @Test
    fun `should return testEnvironment false by default`() {
        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = PaymentDataRequest.fromJson("{}")
        ).build()
            .testEnvironment shouldBe false
    }

    @Test
    fun `should return paymentData`() {
        val actualPaymentData = PaymentDataRequest.fromJson("{}")
        GooglePayConfigBuilder(
            order = "394f2c04-430c-4102-81e6-451d79234fc8",
            paymentData = actualPaymentData
        ).build().paymentData shouldBe actualPaymentData
    }
}
