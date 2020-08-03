package ru.rbs.mobile.payment.sdk.validation

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.test.core.getString
import ru.rbs.mobile.payment.sdk.test.core.targetContext

class CardExpiryValidatorTest {

    private lateinit var cardExpiryValidator: CardExpiryValidator

    @Before
    fun setUp() {
        cardExpiryValidator = CardExpiryValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectCode() {
        val result = cardExpiryValidator.validate("12/29")

        assertEquals(true, result.isValid)
        Assert.assertNull(result.errorMessage)
    }

    @Test
    fun shouldNotAcceptLessThanMinLength() {
        val result = cardExpiryValidator.validate("12")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptMoreThanMaxLength() {
        val result = cardExpiryValidator.validate("12/346")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptMoreIncorrectFormat() {
        val result = cardExpiryValidator.validate("12346")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptEmptyValue() {
        val result = cardExpiryValidator.validate("")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptIncorrectMonth() {
        val result = cardExpiryValidator.validate("13/25")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptIncorrectLastYear() {
        val result = cardExpiryValidator.validate("13/19")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptIncorrectOverTenYears() {
        val result = cardExpiryValidator.validate("13/31")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
    }
}
