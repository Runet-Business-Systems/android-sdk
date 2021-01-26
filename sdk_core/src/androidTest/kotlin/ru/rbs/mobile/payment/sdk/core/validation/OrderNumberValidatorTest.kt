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

class OrderNumberValidatorTest {

    @get:Rule
    val permissionRule: TestRule = grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private lateinit var orderNumberValidator: OrderNumberValidator

    @Before
    fun setUp() {
        orderNumberValidator = OrderNumberValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectOrderNumber() {
        val result = orderNumberValidator.validate("39ce26e1-5fd0-4784-9e6c-25c9f2c2d09e")

        assertEquals(true, result.isValid)
        assertNull(result.errorMessage)
        assertNull(result.errorCode)
    }

    @Test
    fun shouldNotAcceptEmptyOrderNumber() {
        val result = orderNumberValidator.validate("")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_order_incorrect_length), result.errorMessage)
        assertEquals(ValidationCodes.required, result.errorCode)
    }

    @Test
    fun shouldNotAcceptBlankOrderNumber() {
        val result = orderNumberValidator.validate("    ")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_order_incorrect_length), result.errorMessage)
        assertEquals(ValidationCodes.required, result.errorCode)
    }

    @Test
    fun shouldNotAcceptWithSpaceOrderNumber() {
        val result = orderNumberValidator.validate("  39ce26e1 -5fd 0-4784-9e6c-25c9f2c2d09e")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_order_incorrect_length), result.errorMessage)
        assertEquals(ValidationCodes.invalid, result.errorCode)
    }
}
