package ru.rbs.mobile.payment.sdk.core.validation

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import ru.rbs.mobile.payment.sdk.core.R
import ru.rbs.mobile.payment.sdk.core.test.getString
import ru.rbs.mobile.payment.sdk.core.test.targetContext

class PubKeyValidatorTest {

    private lateinit var pubKeyValidator: PubKeyValidator
    @Suppress("MaxLineLength")
    private val testPubKey =
        "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAij/G3JVV3TqYCFZTPmwi4JduQMsZ2HcFLBBA9fYAvApv3FtA+zKdUGgKh/OPbtpsxe1C57gIaRclbzMoafTb0eOdj+jqSEJMlVJYSiZ8Hn6g67evhu9wXh5ZKBQ1RUpqL36LbhYnIrP+TEGR/VyjbC6QTfaktcRfa8zRqJczHFsyWxnlfwKLfqKz5wSqXkShcrwcfRJCyDRjZX6OFUECHsWVK3WMcOV3WZREwbCkh/o5R5Vl6xoyLvSqVEKQiHupJcZu9UEOJiP3yNCn9YPgyFs2vrCeg6qxDPFnCfetcDCLjjLenGF7VyZzBJ9G2NP3k/mNVtD8Kl7lpiurwY7EZwIDAQAB-----END PUBLIC KEY-----"


    @Before
    fun setUp() {
        pubKeyValidator = PubKeyValidator(targetContext())
    }

    @Test
    fun shouldAcceptCorrectPubKey() {
        val result = pubKeyValidator.validate(testPubKey)

        assertEquals(true, result.isValid)
        assertNull(result.errorMessage)
        assertNull(result.errorCode)
    }

    @Test
    fun shouldNotAcceptEmptyPubKey() {
        val result = pubKeyValidator.validate("")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_pub_key_required), result.errorMessage)
        assertEquals(ValidationCodes.required, result.errorCode)
    }

    @Test
    fun shouldNotAcceptBlankPubKey() {
        val result = pubKeyValidator.validate("        ")

        assertEquals(false, result.isValid)
        assertEquals(getString(R.string.rbs_pub_key_required), result.errorMessage)
        assertEquals(ValidationCodes.required, result.errorCode)
    }
}
