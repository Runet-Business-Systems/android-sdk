package ru.rbs.mobile.payment.sdk.model

import java.io.Serializable
import java.util.*

/**
 * Конфигурация оплаты.
 *
 * @param order идентификатор заказа для оплаты.
 * @param cardSaveOptions настройка опции привязки новой карты после оплаты.
 * @param holderInputOptions настройка опции ввода владельца карты.
 * @param cameraScannerOptions настройка опции сканирования данных карты через камеру.
 * @param cards список привязанных карт.
 * @param uuid идентификатор оплаты.
 * @param timestamp время оплаты.
 * @param locale локаль, в которой должна работать форма оплаты.
 * @param buttonText текст кнопки оплаты.
 * @param bindingCVCRequired обязательный ввод CVC оплачивая ранее сохраненной картой.
 */
data class PaymentConfig internal constructor(
    val order: String,
    val cardSaveOptions: CardSaveOptions,
    val holderInputOptions: HolderInputOptions,
    val cameraScannerOptions: CameraScannerOptions,
    val cards: Set<Card>,
    val uuid: String,
    val timestamp: Long,
    val locale: Locale,
    val buttonText: String?,
    val bindingCVCRequired: Boolean
) : Serializable
