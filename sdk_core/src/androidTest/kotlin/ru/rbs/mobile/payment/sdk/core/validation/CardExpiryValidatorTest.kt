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

class CardExpiryValidatorTest {

    @get:Rule
    val permissionRule: TestRule = grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private lateinit var cardExpiryValidator: CardExpiryValidator

    @Before
    fun setUp() {
        cardExpiryValidator =
            CardExpiryValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectCode() {
        val result = cardExpiryValidator.validate("12/29")

        assertEquals(true, result.isValid)
        assertNull(result.errorMessage)
        assertNull(result.errorCode)
    }

    @Test
    fun shouldNotAcceptLessThanMinLength() {
        val result = cardExpiryValidator.validate("12")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
        assertEquals(ValidationCodes.invalidFormat, result.errorCode)
    }

    @Test
    fun shouldNotAcceptMoreThanMaxLength() {
        val result = cardExpiryValidator.validate("12/346")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
        assertEquals(ValidationCodes.invalidFormat, result.errorCode)
    }

    @Test
    fun shouldNotAcceptMoreIncorrectFormat() {
        val result = cardExpiryValidator.validate("12346")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
        assertEquals(ValidationCodes.invalidFormat, result.errorCode)
    }

    @Test
    fun shouldNotAcceptEmptyValue() {
        val result = cardExpiryValidator.validate("")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
        assertEquals(ValidationCodes.required, result.errorCode)
    }

    @Test
    fun shouldNotAcceptIncorrectMonth() {
        val result = cardExpiryValidator.validate("13/25")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }

    @Test
    fun shouldNotAcceptIncorrectLastYear() {
        val result = cardExpiryValidator.validate("13/19")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }

    @Test
    fun shouldNotAcceptIncorrectOverTenYears() {
        val result = cardExpiryValidator.validate("13/31")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_card_incorrect_expiry), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }
}
