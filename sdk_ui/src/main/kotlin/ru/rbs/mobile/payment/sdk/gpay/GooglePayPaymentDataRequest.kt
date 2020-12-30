@file:Suppress("UndocumentedPublicFunction", "UndocumentedPublicClass", "UndocumentedPublicProperty")

package ru.rbs.mobile.payment.sdk.gpay

import org.json.JSONArray
import org.json.JSONObject
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCheckoutOption.COMPLETE_IMMEDIATE_PURCHASE
import ru.rbs.mobile.payment.sdk.gpay.GooglePayTotalPriceStatus.FINAL
import java.math.BigDecimal


class GooglePayPaymentDataRequest {
    var apiVersion = 2
    var apiVersionMinor = 0
    var merchantInfo = MerchantInfo()
    var transactionInfo = TransactionInfo()
    var allowedPaymentMethods = AllowedPaymentMethods()

    fun toJson(): JSONObject = JSONObject().apply {
        put("apiVersion", apiVersion)
        put("apiVersionMinor", apiVersionMinor)
        put("allowedPaymentMethods", allowedPaymentMethods.toJson())
        put("transactionInfo", transactionInfo.toJson())
        put("merchantInfo", merchantInfo.toJson())
    }

    companion object {
        fun paymentDataRequestCreate(block: GooglePayPaymentDataRequest.() -> Unit): GooglePayPaymentDataRequest =
            GooglePayPaymentDataRequest().apply(block)
    }
}

class MerchantInfo {
    var merchantId: String = ""
    var merchantName: String = ""

    fun toJson(): JSONObject = JSONObject().apply {
        put("merchantId", merchantId)
        put("merchantName", merchantName)
    }

    companion object {
        fun merchantInfoCreate(block: MerchantInfo.() -> Unit): MerchantInfo =
            MerchantInfo().apply(block)
    }
}

class TransactionInfo {
    var totalPrice: BigDecimal = BigDecimal.ZERO
    var totalPriceStatus: GooglePayTotalPriceStatus = FINAL
    var countryCode: String = ""
    var currencyCode: String = ""
    var checkoutOption: GooglePayCheckoutOption = COMPLETE_IMMEDIATE_PURCHASE

    fun toJson(): JSONObject = JSONObject().apply {
        put("totalPrice", totalPrice.toString())
        put("totalPriceStatus", totalPriceStatus.value)
        put("countryCode", countryCode)
        put("currencyCode", currencyCode)
        put("checkoutOption", checkoutOption.value)
    }

    companion object {
        fun transactionInfoCreate(block: TransactionInfo.() -> Unit): TransactionInfo =
            TransactionInfo().apply(block)
    }
}

class AllowedPaymentMethods {
    var methods: MutableSet<PaymentMethod> = mutableSetOf()

    fun toJson(): JSONArray = JSONArray().apply {
        methods.forEach { put(it.toJson()) }
    }

    fun method(block: PaymentMethod.() -> Unit) {
        methods.add(PaymentMethod().apply(block))
    }

    companion object {
        fun allowedPaymentMethodsCreate(block: AllowedPaymentMethods.() -> Unit): AllowedPaymentMethods =
            AllowedPaymentMethods().apply(block)
    }
}

class PaymentMethod {
    var type: GooglePayPaymentMethod = GooglePayPaymentMethod.CARD
    var parameters: PaymentMethodParameters = PaymentMethodParameters()
    var tokenizationSpecification: TokenizationSpecification = TokenizationSpecification()

    fun toJson() = JSONObject().apply {
        put("type", type.value)
        put("parameters", parameters.toJson())
        put("tokenizationSpecification", tokenizationSpecification.toJson())
    }
}

class PaymentMethodParameters {
    var allowedAuthMethods: MutableSet<GooglePayAuthMethod> = mutableSetOf()
    var allowedCardNetworks: MutableSet<GooglePayCardNetwork> = mutableSetOf()

    fun toJson(): JSONObject = JSONObject().apply {
        put("allowedAuthMethods", JSONArray().apply {
            allowedAuthMethods.forEach {
                put(it.value)
            }
        })
        put("allowedCardNetworks", JSONArray().apply {
            allowedCardNetworks.forEach {
                put(it.value)
            }
        })
    }

    companion object {
        fun paymentMethodParametersCreate(block: PaymentMethodParameters.() -> Unit): PaymentMethodParameters =
            PaymentMethodParameters().apply(block)
    }
}

class TokenizationSpecification {
    var type: GoogleTokenizationSpecificationType =
        GoogleTokenizationSpecificationType.PAYMENT_GATEWAY
    var parameters: TokenizationSpecificationParameters = TokenizationSpecificationParameters()

    fun toJson(): JSONObject = JSONObject().apply {
        put("type", type.value)
        put("parameters", parameters.toJson())
    }

    companion object {
        fun tokenizationSpecificationCreate(block: TokenizationSpecification.() -> Unit): TokenizationSpecification =
            TokenizationSpecification().apply(block)
    }
}

class TokenizationSpecificationParameters {
    var gateway: String = ""
    var gatewayMerchantId: String = ""

    fun toJson() = JSONObject().apply {
        put("gateway", gateway)
        put("gatewayMerchantId", gatewayMerchantId)
    }

    companion object {
        fun tokenizationSpecificationParametersCreate(
            block: TokenizationSpecificationParameters.() -> Unit
        ): TokenizationSpecificationParameters =
            TokenizationSpecificationParameters().apply(block)
    }
}
