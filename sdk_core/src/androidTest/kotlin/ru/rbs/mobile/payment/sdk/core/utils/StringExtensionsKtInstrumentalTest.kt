package ru.rbs.mobile.payment.sdk.core.utils

import android.Manifest
import androidx.test.rule.GrantPermissionRule.grant
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import ru.rbs.mobile.payment.sdk.core.model.ExpiryDate

class StringExtensionsKtInstrumentalTest {

    @get:Rule
    val permissionRule: TestRule = grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Test
    fun parseColorShouldConvertShortHexNumber() {
        assertEquals(-1, "#fff".parseColor())
        assertEquals(-5592406, "#aaa".parseColor())
        assertEquals(-5587969, "#abf".parseColor())
    }

    @Test
    fun parseColorShouldConvertLongHexNumber() {
        assertEquals(-1, "#ffffff".parseColor())
        assertEquals(-5592406, "#aaaaaa".parseColor())
        assertEquals(-5587969, "#aabbff".parseColor())
    }

    @Test
    fun toExpDateShouldReturnCorrectValue() {
        assertEquals(ExpiryDate(2000, 1), "01/00".toExpDate())
        assertEquals(ExpiryDate(2001, 1), "01/01".toExpDate())
        assertEquals(ExpiryDate(2009, 2), "02/09".toExpDate())
        assertEquals(ExpiryDate(2009, 3), "03/09".toExpDate())
        assertEquals(ExpiryDate(2009, 4), "04/09".toExpDate())
        assertEquals(ExpiryDate(2009, 5), "05/09".toExpDate())
        assertEquals(ExpiryDate(2009, 6), "06/09".toExpDate())
        assertEquals(ExpiryDate(2019, 7), "07/19".toExpDate())
        assertEquals(ExpiryDate(2020, 8), "08/20".toExpDate())
        assertEquals(ExpiryDate(2021, 9), "09/21".toExpDate())
        assertEquals(ExpiryDate(2022, 10), "10/22".toExpDate())
        assertEquals(ExpiryDate(2022, 11), "11/22".toExpDate())
        assertEquals(ExpiryDate(2022, 12), "12/22".toExpDate())
        assertEquals(ExpiryDate(2099, 12), "12/99".toExpDate())
    }

    @Test(expected = IllegalArgumentException::class)
    fun toExpDateShouldThrowExceptionForEmptyString() {
        "".toExpDate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun toExpDateShouldThrowExceptionForNotDigitsSymbols() {
        "abcd".toExpDate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun toExpDateShouldThrowExceptionForLongString() {
        "01/2004".toExpDate()
    }
}
