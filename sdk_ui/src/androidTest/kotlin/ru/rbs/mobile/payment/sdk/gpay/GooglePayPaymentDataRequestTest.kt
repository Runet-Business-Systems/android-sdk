package ru.rbs.mobile.payment.sdk.gpay

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.rbs.mobile.payment.sdk.gpay.AllowedPaymentMethods.Companion.allowedPaymentMethodsCreate
import ru.rbs.mobile.payment.sdk.gpay.GooglePayAuthMethod.CRYPTOGRAM_3DS
import ru.rbs.mobile.payment.sdk.gpay.GooglePayAuthMethod.PAN_ONLY
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCardNetwork.AMEX
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCardNetwork.DISCOVER
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCardNetwork.INTERAC
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCardNetwork.JCB
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCardNetwork.MASTERCARD
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCardNetwork.VISA
import ru.rbs.mobile.payment.sdk.gpay.GooglePayCheckoutOption.COMPLETE_IMMEDIATE_PURCHASE
import ru.rbs.mobile.payment.sdk.gpay.GooglePayPaymentMethod.CARD
import ru.rbs.mobile.payment.sdk.gpay.GoogleTokenizationSpecificationType.PAYMENT_GATEWAY
import ru.rbs.mobile.payment.sdk.gpay.MerchantInfo.Companion.merchantInfoCreate
import ru.rbs.mobile.payment.sdk.gpay.GooglePayPaymentDataRequest.Companion.paymentDataRequestCreate
import ru.rbs.mobile.payment.sdk.gpay.PaymentMethodParameters.Companion.paymentMethodParametersCreate
import ru.rbs.mobile.payment.sdk.gpay.TokenizationSpecification.Companion.tokenizationSpecificationCreate
import ru.rbs.mobile.payment.sdk.gpay.TokenizationSpecificationParameters.Companion.tokenizationSpecificationParametersCreate
import ru.rbs.mobile.payment.sdk.gpay.TransactionInfo.Companion.transactionInfoCreate
import java.math.BigDecimal

class GooglePayPaymentDataRequestTest {

    @Test
    @Suppress("MaxLineLength")
    fun shouldUseDefaultValues() {
        val configJson = paymentDataRequestCreate {
            allowedPaymentMethods = allowedPaymentMethodsCreate {
                method {
                    type = CARD
                    parameters = paymentMethodParametersCreate {
                        allowedAuthMethods = mutableSetOf(PAN_ONLY, CRYPTOGRAM_3DS)
                        allowedCardNetworks =
                            mutableSetOf(AMEX, DISCOVER, INTERAC, JCB, MASTERCARD, VISA)
                    }
                    tokenizationSpecification = tokenizationSpecificationCreate {
                        type = PAYMENT_GATEWAY
                        parameters = tokenizationSpecificationParametersCreate {
                            gateway = "sberbank"
                            gatewayMerchantId = "sbersafe_test"
                        }
                    }
                }
            }
            transactionInfo = transactionInfoCreate {
                totalPrice = BigDecimal.valueOf(1)
                totalPriceStatus = GooglePayTotalPriceStatus.FINAL
                countryCode = "US"
                currencyCode = "USD"
                checkoutOption = COMPLETE_IMMEDIATE_PURCHASE
            }
            merchantInfo = merchantInfoCreate {
                merchantName = "Example Merchant"
                merchantId = "01234567890123456789"
            }
        }.toJson().toString()

        assertEquals(
            """
            {"apiVersion":2,"apiVersionMinor":0,"allowedPaymentMethods":[{"type":"CARD","parameters":{"allowedAuthMethods":["PAN_ONLY","CRYPTOGRAM_3DS"],"allowedCardNetworks":["AMEX","DISCOVER","INTERAC","JCB","MASTERCARD","VISA"]},"tokenizationSpecification":{"type":"PAYMENT_GATEWAY","parameters":{"gateway":"sberbank","gatewayMerchantId":"sbersafe_test"}}}],"transactionInfo":{"totalPrice":"1","totalPriceStatus":"FINAL","countryCode":"US","currencyCode":"USD","checkoutOption":"COMPLETE_IMMEDIATE_PURCHASE"},"merchantInfo":{"merchantId":"01234567890123456789","merchantName":"Example Merchant"}}
        """.trimIndent(), configJson
        )
    }
}
