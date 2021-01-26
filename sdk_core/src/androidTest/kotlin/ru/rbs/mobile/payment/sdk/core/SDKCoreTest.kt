package ru.rbs.mobile.payment.sdk.core

import android.Manifest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule.grant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import ru.rbs.mobile.payment.sdk.core.model.BindingParams
import ru.rbs.mobile.payment.sdk.core.model.CardParams
import ru.rbs.mobile.payment.sdk.core.model.ParamField
import ru.rbs.mobile.payment.sdk.core.validation.ValidationCodes

class SDKCoreTest {

    @get:Rule
    val permissionRule: TestRule = grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @Suppress("MaxLineLength")
    private val testPubKey =
        "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAij/G3JVV3TqYCFZTPmwi4JduQMsZ2HcFLBBA9fYAvApv3FtA+zKdUGgKh/OPbtpsxe1C57gIaRclbzMoafTb0eOdj+jqSEJMlVJYSiZ8Hn6g67evhu9wXh5ZKBQ1RUpqL36LbhYnIrP+TEGR/VyjbC6QTfaktcRfa8zRqJczHFsyWxnlfwKLfqKz5wSqXkShcrwcfRJCyDRjZX6OFUECHsWVK3WMcOV3WZREwbCkh/o5R5Vl6xoyLvSqVEKQiHupJcZu9UEOJiP3yNCn9YPgyFs2vrCeg6qxDPFnCfetcDCLjjLenGF7VyZzBJ9G2NP3k/mNVtD8Kl7lpiurwY7EZwIDAQAB-----END PUBLIC KEY-----"

    private lateinit var sdkCore: SDKCore

    @Before
    fun setUp() {
        sdkCore = SDKCore(context = InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun shouldGenerateWithCard() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "123",
            expiryMMYY = "12/25",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertNotEquals(null, result.token)
        assertEquals(emptyMap<ParamField, String>(), result.errors)
    }

    @Test
    fun shouldGenerateWithCardWithoutCardHolder() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "123",
            expiryMMYY = "12/25",
            cardHolder = null,
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertNotEquals(null, result.token)
        assertEquals(emptyMap<ParamField, String>(), result.errors)
    }

    @Test
    fun shouldGenerateWithCardWithInvalidSymbolsInCardHolder() {
        val params = CardParams(
                mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
                pan = "5391119268214792",
                cvc = "123",
                expiryMMYY = "12/25",
                cardHolder = "4554Pav",
                pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalidFormat, result.errors[ParamField.CARDHOLDER])
    }

    @Test
    fun shouldGenerateWithCardWithMaxLengthInCardHolder() {
        val params = CardParams(
                mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
                pan = "5391119268214792",
                cvc = "123",
                expiryMMYY = "12/25",
                cardHolder = "G".repeat(31),
                pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalid, result.errors[ParamField.CARDHOLDER])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithEmptyMdOrder() {
        val params = CardParams(
            mdOrder = "",
            pan = "5391119268214792",
            cvc = "123",
            expiryMMYY = "12/25",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.required, result.errors[ParamField.MD_ORDER])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithEmptyPan() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "",
            cvc = "123",
            expiryMMYY = "12/25",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.required, result.errors[ParamField.PAN])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithEmptyCVC() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "",
            expiryMMYY = "12/25",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.required, result.errors[ParamField.CVC])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithEmptyExpiry() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "123",
            expiryMMYY = "",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.required, result.errors[ParamField.EXPIRY])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithEmptyPubKey() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "123",
            expiryMMYY = "12/25",
            cardHolder = "Joe Doe",
            pubKey = ""
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.required, result.errors[ParamField.PUB_KEY])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithInvalidPan() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5INVALID19268PAN14792",
            cvc = "123",
            expiryMMYY = "12/25",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalidFormat, result.errors[ParamField.PAN])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithInvalidCVC() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "1AA",
            expiryMMYY = "12/25",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalid, result.errors[ParamField.CVC])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithInvalidExpiry() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "123",
            expiryMMYY = "DDD",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalidFormat, result.errors[ParamField.EXPIRY])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithOutDateExpiry() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "123",
            expiryMMYY = "12/15",
            cardHolder = "Joe Doe",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalid, result.errors[ParamField.EXPIRY])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithMaxOutDateExpiry() {
        val params = CardParams(
                mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
                pan = "5391119268214792",
                cvc = "123",
                expiryMMYY = "12/35",
                cardHolder = "Joe Doe",
                pubKey = testPubKey
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalid, result.errors[ParamField.EXPIRY])
    }

    @Test
    fun shouldReturnErrorWhileGenerateWithCardWithInvalidPubKey() {
        val params = CardParams(
            mdOrder = "c400b41a-aa3d-43db-8727-ac4ca9e8f701",
            pan = "5391119268214792",
            cvc = "123",
            expiryMMYY = "12/25",
            cardHolder = "Joe Doe",
            pubKey = "INVALIDPUBKEY"
        )

        val result = sdkCore.generateWithCard(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalid, result.errors[ParamField.PUB_KEY])
    }

    @Test
    fun shouldGenerateWithBinding() {
        val params = BindingParams(
            mdOrder = "39ce26e1-5fd0-4784-9e6c-25c9f2c2d09e",
            bindingID = "513b17f4-e32e-414f-8c74-936fd7027baa",
            cvc = "123",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithBinding(params)
        assertNotEquals(null, result.token)
        assertEquals(emptyMap<ParamField, String>(), result.errors)
    }

    @Test
    fun shouldGenerateWithBindingWithoutCVC() {
        val params = BindingParams(
            mdOrder = "39ce26e1-5fd0-4784-9e6c-25c9f2c2d09e",
            bindingID = "513b17f4-e32e-414f-8c74-936fd7027baa",
            cvc = null,
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithBinding(params)
        assertNotEquals(null, result.token)
        assertEquals(emptyMap<ParamField, String>(), result.errors)
    }

    @Test
    fun shouldReturnErrorGenerateWithBindingWithEmptyMdOrder() {
        val params = BindingParams(
            mdOrder = "",
            bindingID = "513b17f4-e32e-414f-8c74-936fd7027baa",
            cvc = "123",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithBinding(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.required, result.errors[ParamField.MD_ORDER])
    }

    @Test
    fun shouldReturnErrorGenerateWithBindingWithEmptyBindingID() {
        val params = BindingParams(
            mdOrder = "39ce26e1-5fd0-4784-9e6c-25c9f2c2d09e",
            bindingID = "",
            cvc = "123",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithBinding(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.required, result.errors[ParamField.BINDING_ID])
    }

    @Test
    fun shouldReturnErrorGenerateWithBindingWithEmptyPubKey() {
        val params = BindingParams(
            mdOrder = "39ce26e1-5fd0-4784-9e6c-25c9f2c2d09e",
            bindingID = "513b17f4-e32e-414f-8c74-936fd7027baa",
            cvc = "123",
            pubKey = ""
        )

        val result = sdkCore.generateWithBinding(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.required, result.errors[ParamField.PUB_KEY])
    }

    @Test
    fun shouldReturnErrorGenerateWithBindingWithInvalidCVC() {
        val params = BindingParams(
            mdOrder = "39ce26e1-5fd0-4784-9e6c-25c9f2c2d09e",
            bindingID = "513b17f4-e32e-414f-8c74-936fd7027baa",
            cvc = "aaD",
            pubKey = testPubKey
        )

        val result = sdkCore.generateWithBinding(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalid, result.errors[ParamField.CVC])
    }

    @Test
    fun shouldReturnErrorGenerateWithBindingWithInvalidPubKey() {
        val params = BindingParams(
            mdOrder = "39ce26e1-5fd0-4784-9e6c-25c9f2c2d09e",
            bindingID = "513b17f4-e32e-414f-8c74-936fd7027baa",
            cvc = "123",
            pubKey = "INVALIDPUBKEY"
        )

        val result = sdkCore.generateWithBinding(params)
        assertEquals(null, result.token)
        assertEquals(true, result.errors.isNotEmpty())
        assertEquals(ValidationCodes.invalid, result.errors[ParamField.PUB_KEY])
    }
}
