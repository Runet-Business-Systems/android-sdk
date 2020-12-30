package ru.rbs.mobile.payment.sdk

/**
 * Базовая ошибка, которая может возвращаться в ответе при выполнении методов SDK.
 *
 * @param message описание ошибки.
 * @param cause причина ошибки.
 */
class SDKException(message: String? = null, cause: Throwable? = null) :
    RuntimeException(message, cause)
