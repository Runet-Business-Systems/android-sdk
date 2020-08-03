package ru.rbs.mobile.payment.sdk

/**
 * Интерфейс для обработки результата операции, которая возвращает [ResultType] или [Exception].
 */
interface ResultCallback<ResultType> {

    /**
     * Вызывается при успешном выполнении операции, результатом которой является [result].
     */
    fun onSuccess(result: ResultType)

    /**
     * Вызывается при возникновении ошибки во время выполнения операции. [e] содержит описание
     * ошибки.
     */
    fun onFail(e: Exception)
}
