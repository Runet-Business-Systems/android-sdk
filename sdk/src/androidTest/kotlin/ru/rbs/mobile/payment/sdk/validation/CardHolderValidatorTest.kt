package ru.rbs.mobile.payment.sdk.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.test.core.getString
import ru.rbs.mobile.payment.sdk.test.core.targetContext

class CardHolderValidatorTest {

    private lateinit var cardHolderValidator: CardHolderValidator

    @Before
    fun setUp() {
        cardHolderValidator = CardHolderValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectName() {
        assertTrue(cardHolderValidator.validate("John Doe").isValid)
        assertTrue(cardHolderValidator.validate("Diana Anika").isValid)
    }

    @Test
    fun shouldNotAcceptIncorrectName() {
        val result = cardHolderValidator.validate("Guðmundur Halima")
        assertFalse(result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_card_holder), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptDigits() {
        val result = cardHolderValidator.validate("665361 165654")
        assertFalse(result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_card_holder), result.errorMessage)
    }

    @Test
    fun shouldNotAcceptMoreThanMaxLength() {
        val result = cardHolderValidator.validate("John DoeEEEEEEEEEEEEEEEEEEEEEEE")
        assertFalse(result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_card_holder), result.errorMessage)
    }
}
