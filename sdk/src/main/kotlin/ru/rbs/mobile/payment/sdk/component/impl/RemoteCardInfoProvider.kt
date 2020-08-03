package ru.rbs.mobile.payment.sdk.component.impl

import org.json.JSONObject
import ru.rbs.mobile.payment.sdk.component.CardInfo
import ru.rbs.mobile.payment.sdk.component.CardInfoProvider
import ru.rbs.mobile.payment.sdk.component.CardInfoProviderException
import ru.rbs.mobile.payment.sdk.utils.executePostJson
import ru.rbs.mobile.payment.sdk.utils.responseBodyToJsonObject
import java.net.URL

/**
 * Реализация провайдера получения информации о карте с удаленного сервера.
 *
 * @param url адрес метода для получения информации.
 * @param urlBin префикс для получения бинарных данных.
 */
class RemoteCardInfoProvider(
    private var url: String,
    private var urlBin: String
) : CardInfoProvider {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun resolve(bin: String): CardInfo = try {
        val body = JSONObject(mapOf("bin" to bin)).toString()
        val connection = URL(url).executePostJson(body)
        val info = CardInfo.fromJson(connection.responseBodyToJsonObject())
        info.copy(
            logo = urlBin + info.logo,
            logoInvert = urlBin + info.logoInvert
        )
    } catch (cause: Exception) {
        throw CardInfoProviderException("Error while load card info", cause)
    }
}
