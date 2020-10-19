package ru.rbs.mobile.payment.sdk.component.impl

import ru.rbs.mobile.payment.sdk.component.PaymentStringProcessor
import ru.rbs.mobile.payment.sdk.model.CardBindingIdIdentifier
import ru.rbs.mobile.payment.sdk.model.CardInfo
import ru.rbs.mobile.payment.sdk.model.CardPanIdentifier
import ru.rbs.mobile.payment.sdk.model.ExpiryDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * Реализация процессора для формирования строки с платежной информацией по шаблону.
 */
class DefaultPaymentStringProcessor : PaymentStringProcessor {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)

    /**
     * Формирует список платежных данных по шаблону.
     *
     * Доступные варианты шаблона:
     *
     * • timestamp/UUID/PAN/CVV/EXPDATE/mdOrder
     * • timestamp/UUID/PAN//EXPDATE/mdOrder
     * • timestamp/UUID/PAN///mdOrder
     * • timestamp/UUID//CVV/mdOrder/bindingId
     * • timestamp/UUID///mdOrder/bindingId
     *
     * @param order идентификатор заказа.
     * @param timestamp дата запроса.
     * @param uuid идентификатор в стандарте UUID.
     * @param cardInfo информация о карте списания денежных средств.
     *
     */
    override fun createPaymentString(
        order: String,
        timestamp: Long,
        uuid: String,
        cardInfo: CardInfo
    ): String {
        val cardIdentifier = cardInfo.identifier
        val bindingId: String =
            if (cardIdentifier is CardBindingIdIdentifier) cardIdentifier.value else ""
        val sb = StringBuilder().apply {
            append(Date(timestamp).format())
            append(SPLASH)
            append(uuid)
            append(SPLASH)
            append(if (cardIdentifier is CardPanIdentifier) cardIdentifier.value else "")
            append(SPLASH)
            append(cardInfo.cvv ?: "")
            append(SPLASH)
            append(cardInfo.expDate?.format() ?: "")
            append(SPLASH)
            append(order)
            if (bindingId.isNotBlank()) {
                append(SPLASH)
                append(bindingId)
            }
        }
        return sb.toString()
    }

    private fun ExpiryDate.format(): String = "$expYear$expMonth"

    private fun Date.format(): String = dateFormatter.format(this).let {
        it.substring(0, it.length - 2) + ':' + it.substring(it.length - 2)
    }

    companion object {
        private const val SPLASH = "/"
    }
}
