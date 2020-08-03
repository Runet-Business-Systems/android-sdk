package ru.rbs.mobile.payment.sdk.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.test.core.getString
import ru.rbs.mobile.payment.sdk.test.core.targetContext

class CardNumberValidatorTest {

    private lateinit var cardNumberValidator: CardNumberValidator

    @Before
    fun setUp() {
        cardNumberValidator =
            CardNumberValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectNumber() {
        assertTrue(cardNumberValidator.validate("4556733604106746").isValid)
        assertTrue(cardNumberValidator.validate("4539985984741055997").isValid)
    }

    @Test
    fun shouldNotAcceptLessThanMinLength() {
        cardNumberValidator.validate("455673360410674").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_length), it.errorMessage)
        }
    }

    @Test
    fun shouldNotAcceptMoreThanMaxLength() {
        cardNumberValidator.validate("45399859847410559971").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_length), it.errorMessage)
        }
    }

    @Test
    fun shouldNotAcceptIfLunhFailed() {
        cardNumberValidator.validate("4532047793306966344").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_number), it.errorMessage)
        }

        cardNumberValidator.validate("4556733604106745").let {
            assertFalse(it.isValid)
            assertEquals(getString(R.string.rbs_card_incorrect_number), it.errorMessage)
        }
    }
}
