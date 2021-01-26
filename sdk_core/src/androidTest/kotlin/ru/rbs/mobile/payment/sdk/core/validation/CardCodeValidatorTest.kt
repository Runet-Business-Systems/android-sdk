package ru.rbs.mobile.payment.sdk.core.validation

import android.Manifest
import androidx.test.rule.GrantPermissionRule.grant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.test.getString
import ru.rbs.mobile.payment.sdk.core.test.targetContext

class CardCodeValidatorTest {

    @get:Rule
    val permissionRule: TestRule = grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private lateinit var cardCodeValidator: CardCodeValidator

    @Before
    fun setUp() {
        cardCodeValidator =
            CardCodeValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectCode() {
        val result = cardCodeValidator.validate("123")

        assertEquals(true, result.isValid)
        assertNull(result.errorMessage)
        assertNull(result.errorCode)
    }

    @Test
    fun shouldNotAcceptEmptyString() {
        val result = cardCodeValidator.validate("")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_cvc), result.errorMessage)
        assertEquals(ValidationCodes.required, result.errorCode)
    }

    @Test
    fun shouldNotAcceptLessThanMinLength() {
        val result = cardCodeValidator.validate("12")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_cvc), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }

    @Test
    fun shouldNotAcceptMoreThanMaxLength() {
        val result = cardCodeValidator.validate("1234")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_cvc), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }

    @Test
    fun shouldNotAcceptWithLetterSymbols() {
        val result = cardCodeValidator.validate("1AAA")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_cvc), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }
}
