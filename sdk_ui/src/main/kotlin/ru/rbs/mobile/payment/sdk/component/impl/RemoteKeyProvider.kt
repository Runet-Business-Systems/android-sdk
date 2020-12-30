package ru.rbs.mobile.payment.sdk.component.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ru.rbs.mobile.payment.sdk.component.KeyProvider
import ru.rbs.mobile.payment.sdk.component.KeyProviderException
import ru.rbs.mobile.payment.sdk.core.model.Key
import ru.rbs.mobile.payment.sdk.utils.asList
import ru.rbs.mobile.payment.sdk.utils.executeGet
import ru.rbs.mobile.payment.sdk.utils.responseBodyToJsonObject
import java.net.URL

/**
 * Поставщик ключа на основе внешнего url ресурса.
 *
 * @param url адрес удаленного сервера, предоставляющего активный ключ для шифрования.
 */
class RemoteKeyProvider(private var url: String) : KeyProvider {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun provideKey(): Key = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).executeGet()
            val keys = ActiveKeysDto.fromJson(connection.responseBodyToJsonObject()).keys
            keys.first().toKey()
        } catch (cause: Exception) {
            throw KeyProviderException("Error while load active keys", cause)
        }
    }

    private fun ActiveKeyDto.toKey() = Key(
        value = this.keyValue,
        protocol = this.protocolVersion,
        expiration = this.keyExpiration
    )

    private data class ActiveKeysDto(
        val keys: List<ActiveKeyDto>
    ) {

        companion object {

            fun fromJson(jsonObject: JSONObject): ActiveKeysDto = ActiveKeysDto(
                keys = jsonObject.getJSONArray("keys").asList().map {
                    ActiveKeyDto.fromJson(it)
                }
            )
        }
    }

    private data class ActiveKeyDto(
        val keyValue: String,
        val protocolVersion: String,
        val keyExpiration: Long
    ) {
        companion object {

            fun fromJson(jsonObject: JSONObject): ActiveKeyDto = ActiveKeyDto(
                keyValue = jsonObject.getString("keyValue"),
                protocolVersion = jsonObject.getString("protocolVersion"),
                keyExpiration = jsonObject.getLong("keyExpiration")
            )
        }
    }
}
