package ru.rbs.mobile.payment.sdk.core.validation

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.test.getString
import ru.rbs.mobile.payment.sdk.core.test.targetContext

class OrderNumberValidatorTest {

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
