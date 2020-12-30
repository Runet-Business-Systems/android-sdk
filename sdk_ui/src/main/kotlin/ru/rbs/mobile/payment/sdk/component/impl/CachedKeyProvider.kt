package ru.rbs.mobile.payment.sdk.component.impl

import android.content.SharedPreferences
import ru.rbs.mobile.payment.sdk.component.KeyProvider
import ru.rbs.mobile.payment.sdk.core.model.Key

/**
 * Реализация поставщика ключа с кешированием.
 *
 * @param keyProvider поставщик ключа.
 * @param sharedPreferences хранилище кешированного ключа.
 * @param maxExpiredTime максимальное время кеширования ключа.
 */
class CachedKeyProvider(
    private val keyProvider: KeyProvider,
    private val sharedPreferences: SharedPreferences,
    private val maxExpiredTime: Long = KEY_DEFAULT_MAX_EXPIRE
) : KeyProvider {

    private var innerCachedKey: Key? = null
    private var cachedKey: Key?
        get() {
            if (innerCachedKey == null) {
                innerCachedKey = sharedPreferences.loadKey()
            }
            return innerCachedKey
        }
        set(value) {
            if (value != null) {
                sharedPreferences.save(value)
            } else {
                sharedPreferences.removeKey()
            }
            innerCachedKey = value
        }

    override suspend fun provideKey(): Key = cachedKey.let { key ->
        val now = System.currentTimeMillis()
        if (key != null && key.expiration > now) {
            key
        } else {
            cachedKey = null
            val newKey = keyProvider.provideKey()
            newKey.copy(expiration = newKey.expiration.coerceAtMost(now + maxExpiredTime)).also {
                cachedKey = it
            }
        }
    }

    private fun SharedPreferences.removeKey() = edit()
        .remove(KEY_VALUE)
        .remove(KEY_PROTOCOL)
        .remove(KEY_EXPIRATION)
        .apply()


    private fun SharedPreferences.save(key: Key) = edit()
        .putString(KEY_VALUE, key.value)
        .putString(KEY_PROTOCOL, key.protocol)
        .putLong(KEY_EXPIRATION, key.expiration)
        .apply()

    private fun SharedPreferences.loadKey(): Key? {
        return if (contains(KEY_VALUE) && contains(KEY_PROTOCOL) && contains(KEY_PROTOCOL)) {
            Key(
                value = getString(KEY_VALUE, "")!!,
                protocol = getString(KEY_PROTOCOL, "")!!,
                expiration = getLong(KEY_EXPIRATION, -1)
            )
        } else {
            null
        }
    }

    companion object {
        private const val KEY_VALUE = "public_key_value"
        private const val KEY_PROTOCOL = "public_key_protocol"
        private const val KEY_EXPIRATION = "public_key_expiration"

        /**
         * Максимальное время хранения ключа по умолчанию.
         */
        const val KEY_DEFAULT_MAX_EXPIRE = 86_400_000L // 24 часа
    }
}
