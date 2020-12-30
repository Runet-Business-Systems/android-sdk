package ru.rbs.mobile.payment.sdk.core.model

/**
 * Модель описания ключа для выполнения шифрования платежных данных.
 *
 * @param value значение ключа.
 * @param protocol протокол.
 * @param expiration время окончания срока действия.
 */
data class Key(
    val value: String,
    val protocol: String,
    val expiration: Long
)
