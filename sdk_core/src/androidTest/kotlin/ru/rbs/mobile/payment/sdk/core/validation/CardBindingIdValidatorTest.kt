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

class CardBindingIdValidatorTest {

    @get:Rule
    val permissionRule: TestRule = grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private lateinit var cardBindingIdValidator: CardBindingIdValidator

    @Before
    fun setUp() {
        cardBindingIdValidator = CardBindingIdValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectBindingId() {
        val result = cardBindingIdValidator.validate("513b17f4-e32e-414f-8c74-936fd7027baa")

        assertEquals(true, result.isValid)
        assertNull(result.errorMessage)
        assertNull(result.errorCode)
    }

    @Test
    fun shouldNotAcceptEmptyBindingId() {
        val result = cardBindingIdValidator.validate("")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_binding_required), result.errorMessage)
        assertEquals(ValidationCodes.required, result.errorCode)
    }

    @Test
    fun shouldNotAcceptBlankBindingId() {
        val result = cardBindingIdValidator.validate("   ")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_binding_required), result.errorMessage)
        assertEquals(ValidationCodes.required, result.errorCode)
    }

    @Test
    fun shouldNotAcceptWithSpaceBindingId() {
        val result = cardBindingIdValidator.validate("513b17f4 - e32e-414f-8c74-936fd7027baa")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_binding_incorrect), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }
}
