package ru.rbs.mobile.payment.sdk.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.test.core.getString
import ru.rbs.mobile.payment.sdk.test.core.targetContext

class CardCodeValidatorTest {

    private lateinit var cardCodeValidator: CardCodeValidator

    @Before
    fun setUp() {
        cardCodeValidator = CardCodeValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectCode() {
        val result = cardCodeValidator.validate("123")

        assertEquals(true, result.isValid)
        assertNull(result.errorMessage)
    }

    @Test
    fun shouldNotAcceptLessThanMinLength() {
        val result = cardCodeValidator.validate("12")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_cvc), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptMoreThanMaxLength() {
        val result = cardCodeValidator.validate("1234")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_cvc), result.errorMessage)
    }
}
