package ru.rbs.mobile.payment.sdk.component

/**
 * Ошибка возникающая при получении информации о карте.
 *
 * @param message текст описания ошибки.
 * @param cause причина ошибки.
 */
class CardInfoProviderException(override val message: String, override val cause: Throwable?) :
    RuntimeException(message, cause)
