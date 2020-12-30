package ru.rbs.mobile.payment.sdk.component

import org.json.JSONObject
import ru.rbs.mobile.payment.sdk.utils.asStringList

/**
 * Информация о стиле и типе карты.
 *
 * @param backgroundColor цвет фона.
 * @param backgroundGradient цвета градиента фона.
 * @param backgroundLightness true если фон в светлых тонах, в противном случае false.
 * @param textColor цвет текста на карте, в формате #ffffff или #fff.
 * @param logo ссылка на файл логотипа банка карты.
 * @param logoInvert ссылка на файл логотипа банка карты для светлого фона.
 * @param paymentSystem название платежной системы.
 * @param status статус ответа.
 */
data class CardInfo(
    val backgroundColor: String,
    val backgroundGradient: List<String>,
    val backgroundLightness: Boolean,
    val textColor: String,
    val logo: String,
    val logoInvert: String,
    val paymentSystem: String,
    val status: String
) {

    companion object {

        /**
         * Десериализация [CardInfo] из объекта JSONObject.
         *
         * @param jsonObject json объект с данным для формирования [CardInfo]
         * @return объект с информацией о карте.
         */
        fun fromJson(jsonObject: JSONObject): CardInfo = CardInfo(
            backgroundColor = jsonObject.getString("backgroundColor"),
            backgroundGradient = jsonObject.getJSONArray("backgroundGradient").asStringList(),
            backgroundLightness = jsonObject.getBoolean("backgroundLightness"),
            textColor = jsonObject.getString("textColor"),
            logo = jsonObject.getString("logo"),
            logoInvert = jsonObject.getString("logoInvert"),
            paymentSystem = jsonObject.getString("paymentSystem"),
            status = jsonObject.getString("status")
        )
    }
}
