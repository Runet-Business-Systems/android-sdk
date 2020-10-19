package ru.rbs.mobile.payment.sdk.component.impl

import io.kotest.matchers.shouldBe
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.component.PaymentStringProcessor
import ru.rbs.mobile.payment.sdk.model.CardBindingIdIdentifier
import ru.rbs.mobile.payment.sdk.model.CardInfo
import ru.rbs.mobile.payment.sdk.model.CardPanIdentifier
import ru.rbs.mobile.payment.sdk.model.ExpiryDate
import java.util.*

@Suppress("MaxLineLength")
class DefaultPaymentStringProcessorTest {

    private lateinit var paymentStringProcessor: PaymentStringProcessor

    @Before
    fun setUp() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"))
        paymentStringProcessor = DefaultPaymentStringProcessor()
    }

    @Test
    fun `should return filled template for a new card`() {
        val template = paymentStringProcessor.createPaymentString(
            order = "7f472085-399e-414e-b51c-a7b538aee497",
            timestamp = 1594009580806L,
            uuid = "fd4b1011-727a-41e8-95b4-d7092d729022",
            cardInfo = CardInfo(
                identifier = CardPanIdentifier("4532896701439077"),
                cvv = "444",
                expDate = ExpiryDate(2020, 12)
            )
        )

        template shouldBe "2020-07-06T07:26:20+03:00/fd4b1011-727a-41e8-95b4-d7092d729022/4532896701439077/444/202012/7f472085-399e-414e-b51c-a7b538aee497"
    }

    @Test
    fun `should return filled template for a new card without cvv`() {
        val template = paymentStringProcessor.createPaymentString(
            order = "7f472085-399e-414e-b51c-a7b538aee497",
            timestamp = 1594009580806L,
            uuid = "fd4b1011-727a-41e8-95b4-d7092d729022",
            cardInfo = CardInfo(
                identifier = CardPanIdentifier("4532896701439077"),
                expDate = ExpiryDate(2020, 12)
            )
        )

        template shouldBe "2020-07-06T07:26:20+03:00/fd4b1011-727a-41e8-95b4-d7092d729022/4532896701439077//202012/7f472085-399e-414e-b51c-a7b538aee497"
    }

    @Test
    fun `should return filled template for a new card without exp date`() {
        val template = paymentStringProcessor.createPaymentString(
            order = "7f472085-399e-414e-b51c-a7b538aee497",
            timestamp = 1594009580806L,
            uuid = "fd4b1011-727a-41e8-95b4-d7092d729022",
            cardInfo = CardInfo(
                identifier = CardPanIdentifier("4532896701439077"),
                cvv = "444"
            )
        )

        template shouldBe "2020-07-06T07:26:20+03:00/fd4b1011-727a-41e8-95b4-d7092d729022/4532896701439077/444//7f472085-399e-414e-b51c-a7b538aee497"
    }

    @Test
    fun `should return filled template for a new card without cvv and exp date`() {
        val template = paymentStringProcessor.createPaymentString(
            order = "7f472085-399e-414e-b51c-a7b538aee497",
            timestamp = 1594009580806L,
            uuid = "fd4b1011-727a-41e8-95b4-d7092d729022",
            cardInfo = CardInfo(
                identifier = CardPanIdentifier("4532896701439077")
            )
        )
        template shouldBe "2020-07-06T07:26:20+03:00/fd4b1011-727a-41e8-95b4-d7092d729022/4532896701439077///7f472085-399e-414e-b51c-a7b538aee497"
    }

    @Test
    fun `should return filled template for saved card`() {
        val template = paymentStringProcessor.createPaymentString(
            order = "7f472085-399e-414e-b51c-a7b538aee497",
            timestamp = 1594009580806L,
            uuid = "fd4b1011-727a-41e8-95b4-d7092d729022",
            cardInfo = CardInfo(
                identifier = CardBindingIdIdentifier("47eb0336-5ad9-4e03-8a1e-b9f3656ec768"),
                cvv = "444",
                expDate = ExpiryDate(2020, 12)
            )
        )

        template shouldBe "2020-07-06T07:26:20+03:00/fd4b1011-727a-41e8-95b4-d7092d729022//444/202012/7f472085-399e-414e-b51c-a7b538aee497/47eb0336-5ad9-4e03-8a1e-b9f3656ec768"
    }

    @Test
    fun `should return filled template for saved card without cvv`() {
        val template = paymentStringProcessor.createPaymentString(
            order = "7f472085-399e-414e-b51c-a7b538aee497",
            timestamp = 1594009580806L,
            uuid = "fd4b1011-727a-41e8-95b4-d7092d729022",
            cardInfo = CardInfo(
                identifier = CardBindingIdIdentifier("47eb0336-5ad9-4e03-8a1e-b9f3656ec768"),
                expDate = ExpiryDate(2020, 12)
            )
        )

        template shouldBe "2020-07-06T07:26:20+03:00/fd4b1011-727a-41e8-95b4-d7092d729022///202012/7f472085-399e-414e-b51c-a7b538aee497/47eb0336-5ad9-4e03-8a1e-b9f3656ec768"
    }

    @Test
    fun `should return filled template for saved card without exp date`() {
        val template = paymentStringProcessor.createPaymentString(
            order = "7f472085-399e-414e-b51c-a7b538aee497",
            timestamp = 1594009580806L,
            uuid = "fd4b1011-727a-41e8-95b4-d7092d729022",
            cardInfo = CardInfo(
                identifier = CardBindingIdIdentifier("47eb0336-5ad9-4e03-8a1e-b9f3656ec768"),
                cvv = "444"
            )
        )

        template shouldBe "2020-07-06T07:26:20+03:00/fd4b1011-727a-41e8-95b4-d7092d729022//444//7f472085-399e-414e-b51c-a7b538aee497/47eb0336-5ad9-4e03-8a1e-b9f3656ec768"
    }

    @Test
    fun `should return filled template for saved card without cvv and exp date`() {
        val template = paymentStringProcessor.createPaymentString(
            order = "7f472085-399e-414e-b51c-a7b538aee497",
            timestamp = 1594009580806L,
            uuid = "fd4b1011-727a-41e8-95b4-d7092d729022",
            cardInfo = CardInfo(
                identifier = CardBindingIdIdentifier("47eb0336-5ad9-4e03-8a1e-b9f3656ec768")
            )
        )

        template shouldBe "2020-07-06T07:26:20+03:00/fd4b1011-727a-41e8-95b4-d7092d729022////7f472085-399e-414e-b51c-a7b538aee497/47eb0336-5ad9-4e03-8a1e-b9f3656ec768"
    }
}
