package ru.rbs.mobile.payment.sdk.core.validation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.test.getString
import ru.rbs.mobile.payment.sdk.core.test.targetContext

class CardNumberValidatorTest {

    private lateinit var cardNumberValidator: CardNumberValidator

    @Before
    fun setUp() {
        cardNumberValidator =
            CardNumberValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectNumber() {
        val resultFirst = cardNumberValidator.validate("4556733604106746")
        assertTrue(resultFirst.isValid)
        assertNull(resultFirst.errorMessage)
        assertNull(resultFirst.errorCode)

        val resultSecond = cardNumberValidator.validate("4539985984741055997")
        assertTrue(resultSecond.isValid)
        assertNull(resultSecond.errorMessage)
        assertNull(resultSecond.errorCode)
    }

    @Test
    fun shouldNotAcceptLessThanMinLength() {
        cardNumberValidator.validate("455673360410674").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_length), it.errorMessage)
            assertEquals(ValidationCodes.invalid, it.errorCode)
        }
    }

    @Test
    fun shouldNotAcceptEmptyLine() {
        cardNumberValidator.validate("").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_number), it.errorMessage)
            assertEquals(ValidationCodes.required, it.errorCode)
        }
    }

    @Test
    fun shouldNotAcceptMoreThanMaxLength() {
        cardNumberValidator.validate("45399859847410559971").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_length), it.errorMessage)
            assertEquals(ValidationCodes.invalid, it.errorCode)
        }
    }

    @Test
    fun shouldNotAcceptNotDigits() {
        cardNumberValidator.validate("IncorrectCardNum").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_number), it.errorMessage)
            assertEquals(ValidationCodes.invalidFormat, it.errorCode)
        }
    }

    @Test
    fun shouldNotAcceptIfLunhFailed() {
        cardNumberValidator.validate("4532047793306966344").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_number), it.errorMessage)
            assertEquals(ValidationCodes.invalid, it.errorCode)
        }

        cardNumberValidator.validate("4556733604106745").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_number), it.errorMessage)
            assertEquals(ValidationCodes.invalid, it.errorCode)
        }
    }
}
