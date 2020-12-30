package ru.rbs.mobile.payment.sdk.core.validation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.test.getString
import ru.rbs.mobile.payment.sdk.core.test.targetContext

class CardHolderValidatorTest {

    private lateinit var cardHolderValidator: CardHolderValidator

    @Before
    fun setUp() {
        cardHolderValidator =
            CardHolderValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectName() {
        val resultFirst = cardHolderValidator.validate("John Doe")
        assertTrue(resultFirst.isValid)
        assertNull(resultFirst.errorMessage)
        assertNull(resultFirst.errorCode)

        val secondResult = cardHolderValidator.validate("Diana Anika")
        assertTrue(secondResult.isValid)
        assertNull(secondResult.errorMessage)
        assertNull(secondResult.errorCode)
    }

    @Test
    fun shouldNotAcceptIncorrectName() {
        val result = cardHolderValidator.validate("Guðmundur Halima")
        assertFalse(result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_card_holder), result.errorMessage)
        assertEquals(ValidationCodes.invalidFormat, result.errorCode)
    }

    @Test
    fun shouldNotAcceptDigits() {
        val result = cardHolderValidator.validate("665361 165654")
        assertFalse(result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_card_holder), result.errorMessage)
        assertEquals(ValidationCodes.invalidFormat, result.errorCode)
    }

    @Test
    fun shouldNotAcceptMoreThanMaxLength() {
        val result = cardHolderValidator.validate("John DoeEEEEEEEEEEEEEEEEEEEEEEE")
        assertFalse(result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_card_holder), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }
}
