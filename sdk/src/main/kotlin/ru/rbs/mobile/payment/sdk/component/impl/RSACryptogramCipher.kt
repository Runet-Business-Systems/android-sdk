package ru.rbs.mobile.payment.sdk.component.impl

import android.util.Base64
import ru.rbs.mobile.payment.sdk.component.CryptogramCipher
import ru.rbs.mobile.payment.sdk.model.Key
import ru.rbs.mobile.payment.sdk.utils.pemKeyContent
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Реализация шифровальщика криптограммы.
 */
class RSACryptogramCipher: CryptogramCipher {

    override suspend fun encode(data: String, key: Key): String {
        check(key.protocol == "RSA")
        val keyPem = key.value.pemKeyContent()
        val keyBytes = Base64.decode(keyPem.toByteArray(), Base64.DEFAULT)
        val spec = X509EncodedKeySpec(keyBytes)
        val fact = KeyFactory.getInstance("RSA")

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, fact.generatePublic(spec))
        val encoded = cipher.doFinal(data.toByteArray())

        return Base64.encodeToString(encoded, Base64.NO_WRAP)
    }
}
