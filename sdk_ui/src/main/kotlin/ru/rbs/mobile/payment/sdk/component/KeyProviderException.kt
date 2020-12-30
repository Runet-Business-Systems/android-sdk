package ru.rbs.mobile.payment.sdk.component

/**
 * Ошибка возникающая при получении ключа шифрования.
 *
 * @param message текст описания ошибки.
 * @param cause причина ошибки.
 */
class KeyProviderException(override val message: String, override val cause: Throwable?) :
    RuntimeException(message, cause)
