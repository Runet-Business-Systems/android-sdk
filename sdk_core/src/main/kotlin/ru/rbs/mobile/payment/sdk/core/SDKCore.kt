package ru.rbs.mobile.payment.sdk.core

import android.content.Context
import ru.rbs.mobile.payment.sdk.core.component.CryptogramCipher
import ru.rbs.mobile.payment.sdk.core.component.PaymentStringProcessor
import ru.rbs.mobile.payment.sdk.core.component.impl.DefaultPaymentStringProcessor
import ru.rbs.mobile.payment.sdk.core.component.impl.RSACryptogramCipher
import ru.rbs.mobile.payment.sdk.core.model.BindingParams
import ru.rbs.mobile.payment.sdk.core.model.CardBindingIdIdentifier
import ru.rbs.mobile.payment.sdk.core.model.CardInfo
import ru.rbs.mobile.payment.sdk.core.model.CardPanIdentifier
import ru.rbs.mobile.payment.sdk.core.model.CardParams
import ru.rbs.mobile.payment.sdk.core.model.Key
import ru.rbs.mobile.payment.sdk.core.model.ParamField
import ru.rbs.mobile.payment.sdk.core.utils.toExpDate
import ru.rbs.mobile.payment.sdk.core.validation.CardBindingIdValidator
import ru.rbs.mobile.payment.sdk.core.validation.CardCodeValidator
import ru.rbs.mobile.payment.sdk.core.validation.CardExpiryValidator
import ru.rbs.mobile.payment.sdk.core.validation.CardHolderValidator
import ru.rbs.mobile.payment.sdk.core.validation.CardNumberValidator
import ru.rbs.mobile.payment.sdk.core.validation.OrderNumberValidator
import ru.rbs.mobile.payment.sdk.core.validation.PubKeyValidator
import java.util.*

/**
 * @param context контекст для получения строковых ресурсов.
 */
class SDKCore(context: Context) {

    private val paymentStringProcessor: PaymentStringProcessor = DefaultPaymentStringProcessor()
    private val cryptogramCipher: CryptogramCipher = RSACryptogramCipher()
    private val orderNumberValidator: OrderNumberValidator = OrderNumberValidator(context)
    private val cardExpiryValidator: CardExpiryValidator = CardExpiryValidator(context)
    private val cardNumberValidator: CardNumberValidator = CardNumberValidator(context)
    private val cardBindingIdValidator: CardBindingIdValidator = CardBindingIdValidator(context)
    private val cardCodeValidator: CardCodeValidator = CardCodeValidator(context)
    private val cardHolderValidator: CardHolderValidator = CardHolderValidator(context)
    private val pubKeyValidator: PubKeyValidator = PubKeyValidator(context)

    /**
     * Метод генерации токена для новой карты.
     *
     * @param params информация о новой карте.
     * @return сгенерированный токен или ошибка.
     */
    fun generateWithCard(params: CardParams): TokenResult {

        val validatorsMap = mapOf(
            params.cardHolder to cardHolderValidator,
            params.mdOrder to orderNumberValidator,
            params.expiryMMYY to cardExpiryValidator,
            params.pan to cardNumberValidator,
            params.cvc to cardCodeValidator,
            params.pubKey to pubKeyValidator
        )

        val fieldErrors = mapOf(
            params.cardHolder to ParamField.CARDHOLDER,
            params.mdOrder to ParamField.MD_ORDER,
            params.expiryMMYY to ParamField.EXPIRY,
            params.pan to ParamField.PAN,
            params.cvc to ParamField.CVC,
            params.pubKey to ParamField.PUB_KEY
        )

        for ((fieldValue, validator) in validatorsMap) {
            if (fieldValue != null) {
                validator.validate(fieldValue).takeIf { !it.isValid }?.let {
                    return TokenResult.withErrors(
                        mapOf(
                            (fieldErrors[fieldValue] ?: error(ParamField.UNKNOWN)) to it.errorCode!!
                        )
                    )
                }
            }
        }

        val cardInfo = CardInfo(
            identifier = CardPanIdentifier(value = params.pan),
            cvv = params.cvc,
            expDate = params.expiryMMYY.toExpDate()
        )
        return prepareToken(params.mdOrder, cardInfo, params.pubKey)
    }

    /**
     * Метод генерации токена для сохраненной карты.
     *
     * @param params информация о привязанной карте.
     * @return сгенерированный токен или ошибка.
     */
    fun generateWithBinding(params: BindingParams): TokenResult {
        val validatorsMap = mapOf(
            params.mdOrder to orderNumberValidator,
            params.bindingID to cardBindingIdValidator,
            params.cvc to cardCodeValidator,
            params.pubKey to pubKeyValidator
        )

        val fieldErrors = mapOf(
            params.mdOrder to ParamField.MD_ORDER,
            params.bindingID to ParamField.BINDING_ID,
            params.cvc to ParamField.CVC,
            params.pubKey to ParamField.PUB_KEY
        )

        for ((fieldValue, validator) in validatorsMap) {
            if (fieldValue != null) {
                validator.validate(fieldValue).takeIf { !it.isValid }?.let {
                    return TokenResult.withErrors(
                        mapOf(
                            (fieldErrors[fieldValue] ?: error(ParamField.UNKNOWN)) to it.errorCode!!
                        )
                    )
                }
            }
        }

        val cardInfo = CardInfo(
            identifier = CardBindingIdIdentifier(value = params.bindingID),
            cvv = params.cvc
        )
        return prepareToken(params.mdOrder, cardInfo, params.pubKey)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun prepareToken(
        mdOrder: String,
        cardInfo: CardInfo,
        pubKey: String
    ): TokenResult {
        val paymentString = paymentStringProcessor.createPaymentString(
            order = mdOrder,
            timestamp = System.currentTimeMillis(),
            uuid = UUID.randomUUID().toString(),
            cardInfo = cardInfo
        )
        val key = Key(
            value = pubKey,
            protocol = "RSA",
            expiration = Long.MAX_VALUE
        )
        return try {
            val token = cryptogramCipher.encode(paymentString, key)
            TokenResult.withToken(token)
        } catch (e: IllegalArgumentException) {
            TokenResult.withErrors(mapOf(ParamField.PUB_KEY to "invalid"))
        } catch (e: Exception) {
            TokenResult.withErrors(mapOf(ParamField.UNKNOWN to "unknown"))
        }
    }
}
