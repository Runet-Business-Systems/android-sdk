package ru.rbs.mobile.payment.sdk.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsKtInstrumentalTest {

    @Test
    fun parseColorShouldConvertShortHexNumber() {
        assertEquals(-1, "#fff".parseColor())
        assertEquals(-5592406, "#aaa".parseColor())
        assertEquals(-5587969, "#abf".parseColor())
    }

    @Test
    fun parseColorShouldConvertLongHexNumber() {
        assertEquals(-1,  "#ffffff".parseColor())
        assertEquals(-5592406,  "#aaaaaa".parseColor())
        assertEquals(-5587969,  "#aabbff".parseColor())
    }
}
