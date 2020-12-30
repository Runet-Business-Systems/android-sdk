package ru.rbs.mobile.payment.sdk.utils

import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


/**
 * Код успешного ответа сервера.
 */
const val CODE_SUCCESS = 200

/**
 * Приведение ответа к объекту [JSONObject].
 *
 * @return экземпляр [JSONObject] тела успешного ответа или тела ошибки.
 */
fun HttpURLConnection.responseBodyToJsonObject(): JSONObject {
    val br = BufferedReader(
        InputStreamReader(
            if (responseCode == CODE_SUCCESS) inputStream else errorStream
        )
    )
    val responseBody = br.use(BufferedReader::readText)
    return JSONObject(responseBody)
}

/**
 * Выполнение GET запроса на URL экземпляре.
 *
 * @return возвращает [HttpURLConnection].
 */
fun URL.executeGet(): HttpURLConnection = (openConnection() as HttpURLConnection).apply {
    requestMethod = "GET"
    setChunkedStreamingMode(0)
}

/**
 * Расширение для выполнения POST запроса.
 *
 * @param jsonBody json для отправки в теле запроса.
 * @return возвращает [HttpURLConnection].
 */
fun URL.executePostJson(jsonBody: String): HttpURLConnection =
    (openConnection() as HttpURLConnection).apply {
        requestMethod = "POST"
        setRequestProperty("Content-Type", "application/json")
        doOutput = true
        setChunkedStreamingMode(0)
        OutputStreamWriter(BufferedOutputStream(outputStream)).use {
            it.write(jsonBody)
            it.flush()
            it.close()
        }
    }
