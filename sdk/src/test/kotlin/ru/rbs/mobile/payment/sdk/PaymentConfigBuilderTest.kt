package ru.rbs.mobile.payment.sdk

import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.comparables.shouldNotBeEqualComparingTo
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.beEmpty
import org.junit.Test
import ru.rbs.mobile.payment.sdk.model.Card
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions

class PaymentConfigBuilderTest {

    @Test
    fun `should return order number`() {
        val expectedOrder = "394f2c04-430c-4102-81e6-451d79234fc8"

        val actualOrder = PaymentConfigBuilder(expectedOrder)
            .build()
            .order

        actualOrder shouldBe expectedOrder
    }

    @Test
    fun `should return card save options as HIDE by default`() {
        val cardSaveOptions = PaymentConfigBuilder("18818587-aa98-4149-8780-3816afbd67f7")
            .build()
            .cardSaveOptions

        cardSaveOptions shouldBe CardSaveOptions.HIDE
    }

    @Test
    fun `should return defined card save options as NO_BY_DEFAULT`() {
        val cardSaveOptions = PaymentConfigBuilder("00d46e7d-ee70-4a8f-95d1-6da9c52d7473")
            .cardSaveOptions(CardSaveOptions.NO_BY_DEFAULT)
            .build()
            .cardSaveOptions

        cardSaveOptions shouldBe CardSaveOptions.NO_BY_DEFAULT
    }

    @Test
    fun `should return defined card save options as YES_BY_DEFAULT`() {
        val cardSaveOptions = PaymentConfigBuilder("632c6bb5-5917-44bc-b73d-db78145e2985")
            .cardSaveOptions(CardSaveOptions.YES_BY_DEFAULT)
            .build()
            .cardSaveOptions

        cardSaveOptions shouldBe CardSaveOptions.YES_BY_DEFAULT
    }

    @Test
    fun `should return empty cards by default`() {
        val cards = PaymentConfigBuilder("2c04b14c-136b-40e3-bddc-9c371bf7848a")
            .build()
            .cards

        cards shouldBe emptySet()
    }

    @Test
    fun `should return defined cards`() {
        val expectedCards = setOf(
            Card("492980xxxxxx7724", "ee199a55-cf16-41b2-ac9e-cc1c731edd19")
        )

        val actualCards = PaymentConfigBuilder("08aba475-1291-4f22-b593-e359487b431d")
            .cards(expectedCards)
            .build()
            .cards

        actualCards shouldBe expectedCards
    }

    @Test
    fun `should return empty buttonText by default`() {
        val buttonText = PaymentConfigBuilder("fefdc0a0-1c7e-4a28-81d4-63384205b266")
            .build()
            .buttonText

        buttonText should beNull()
    }

    @Test
    fun `should return defined buttonText`() {
        val expectedButtonText = "Pay"

        val actualButtonText = PaymentConfigBuilder("24bcdfe5-1683-4108-b1c9-1970f40401c2")
            .buttonText(expectedButtonText)
            .build()
            .buttonText

        actualButtonText shouldBe expectedButtonText
    }

    @Test
    fun `should return generated uuid value by default`() {
        val order = "2545f609-490b-4c8d-a3fb-be1f8d30fa77"

        val firstUUID = PaymentConfigBuilder(order)
            .build()
            .uuid
        val secondUUID = PaymentConfigBuilder(order)
            .build()
            .uuid

        firstUUID shouldNot beEmpty()
        secondUUID shouldNot beEmpty()
        firstUUID shouldNotBeEqualComparingTo secondUUID
    }

    @Test
    fun `should return defined uuid`() {
        val expectedUuid = "62d0bb9e-111b-4c28-a79e-e7fb8d1791eb"

        val actualUuid = PaymentConfigBuilder("701e6250-fab1-403d-a6d0-10267c5faf6f")
            .cardSaveOptions(CardSaveOptions.NO_BY_DEFAULT)
            .uuid(expectedUuid)
            .build()
            .uuid

        actualUuid shouldBe expectedUuid
    }

    @Test
    fun `should return current timestamp by default`() {
        val order = "039e5501-d39b-47c1-a0e5-bd1d3ae917ad"

        val beforeExecute = System.currentTimeMillis()
        Thread.sleep(100)
        val firstTimestamp = PaymentConfigBuilder(order)
            .build()
            .timestamp
        Thread.sleep(100)
        val secondTimestamp = PaymentConfigBuilder(order)
            .build()
            .timestamp
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

        val actualTimestamp = PaymentConfigBuilder("455152ee-25c5-4aad-bbd6-6b408b265d1d")
            .timestamp(expectedTimestamp)
            .build()
            .timestamp

        actualTimestamp shouldBe expectedTimestamp
    }

    @Test
    fun `should return same value of timestamp for the same builder`() {
        val builder = PaymentConfigBuilder("5eec2dec-b86a-48b3-b296-a772eb5ff77f")

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
    fun `should return same generated value of uuid for the same builder`() {
        val builder = PaymentConfigBuilder("3d73539f-2bc1-4dc6-8575-d36789af74e4")

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
}
