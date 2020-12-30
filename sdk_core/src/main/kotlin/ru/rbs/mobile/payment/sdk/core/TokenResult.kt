package ru.rbs.mobile.payment.sdk.core

import ru.rbs.mobile.payment.sdk.core.model.ParamField

/**
 *  Описание токена.
 *
 *  @param token токен в виде строки.
 *  @param errors ошибка при генерации токена.
 */
data class TokenResult private constructor(
    val token: String?,
    val errors: Map<ParamField, String>
) {

    companion object {
        /**
         *  Метод для возврата токена.
         *
         *  @param token сгенерированный токен.
         *  @return полученный токен.
         */
        fun withToken(token: String): TokenResult = TokenResult(token = token, errors = emptyMap())

        /**  Метод для возврата ошибки.
        *
        *  @param errors ошибки с их описанием полученные при генерации токена.
        *  @return ошибка при генерации токена.
        */
        fun withErrors(errors: Map<ParamField, String>): TokenResult =
            TokenResult(token = null, errors = errors)
    }
}
