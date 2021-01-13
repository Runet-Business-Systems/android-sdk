package ru.rbs.mobile.payment.sample.kotlin.threeds

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpRedirect
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.timeout
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Класс предоставляет реализацию необходимых методов для проведения оплаты платежа.
 */
class ThreeDSGatewayApi {

    private val httpClient by lazy {
        HttpClient {
            install(HttpTimeout) {
                connectTimeoutMillis = 8_000
                requestTimeoutMillis = 8_000
                socketTimeoutMillis = 8_000
            }
            install(HttpRedirect) {
                checkHttpMethod = false
                allowHttpsDowngrade = true
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                serializer = KotlinxSerializer(json)
            }
        }
    }

    suspend fun executeRegisterOrder(
        url: String,
        request: RegisterRequest
    ): RegisterOrderResponse {
        return withContext(Dispatchers.IO) {
            httpClient.post(url) {
                body = FormDataContent(
                    Parameters.build {
                        append("amount", request.amount)
                        append("userName", request.userName)
                        append("password", request.password)
                        append("returnUrl", request.returnUrl)
                        append("failUrl", request.failUrl)
                        append("email", request.email)
                    }
                )
            }
        }
    }

    suspend fun executePaymentOrder(
        url: String,
        request: PaymentOrderRequest
    ): PaymentOrderResponse {
        return withContext(Dispatchers.IO) {
            httpClient.post(url) {
                body = FormDataContent(
                    Parameters.build {
                        append("seToken", request.seToken)
                        append("MDORDER", request.mdOrder)
                        append("userName", request.userName)
                        append("password", request.password)
                        append("TEXT", request.text)
                        append("threeDSSDK", request.threeDSSDK.toString())
                    }
                )
            }
        }
    }

    suspend fun executePaymentOrderSecondStep(
        url: String,
        request: PaymentOrderSecondStepRequest
    ): PaymentOrderSecondStepResponse {
        return withContext(Dispatchers.IO) {
            httpClient.post(url) {
                body = FormDataContent(
                    Parameters.build {
                        append("seToken", request.seToken)
                        append("MDORDER", request.mdOrder)
                        append("userName", request.userName)
                        append("password", request.password)
                        append("TEXT", request.text)
                        append("threeDSSDK", request.threeDSSDK.toString())
                        append("threeDSSDKEncData", request.threeDSSDKEncData)
                        append("threeDSSDKEphemPubKey", request.threeDSSDKEphemPubKey)
                        append("threeDSSDKAppId", request.threeDSSDKAppId)
                        append("threeDSSDKTransId", request.threeDSSDKTransId)
                        append("threeDSServerTransId", request.threeDSServerTransId)
                        append("threeDSSDKReferenceNumber", request.threeDSSDKReferenceNumber)
                    }
                )
            }
        }
    }

    suspend fun executeFinishOrder(
        url: String,
        request: PaymentFinishOrderRequest
    ): FinishOrderResponse {
        return withContext(Dispatchers.IO) {
            httpClient.post(url) {
                timeout {
                    connectTimeoutMillis = 4_000
                    requestTimeoutMillis = 4_000
                    socketTimeoutMillis = 4_000
                }
                body = FormDataContent(
                    Parameters.build {
                        append("threeDSServerTransId", request.tDsTransId)
                        append("userName", request.userName)
                        append("password", request.password)
                    }
                )
            }
        }
    }

    suspend fun executeCheckOrderStatus(
        url: String,
        request: PaymentCheckOrderStatusRequest
    ): String {
        return withContext(Dispatchers.IO) {
            httpClient.post(url) {
                body = FormDataContent(
                    Parameters.build {
                        append("orderId", request.orderId)
                        append("userName", request.userName)
                        append("password", request.password)
                    }
                )
            }
        }
    }

    data class RegisterRequest(
        val amount: String,
        val userName: String,
        val password: String,
        val returnUrl: String,
        val failUrl: String,
        val email: String
    )

    data class PaymentOrderRequest(
        val seToken: String,
        val mdOrder: String,
        val userName: String,
        val password: String,
        val text: String,
        val threeDSSDK: Boolean
    )

    data class PaymentOrderSecondStepRequest(
        val seToken: String,
        val mdOrder: String,
        val userName: String,
        val password: String,
        val text: String,
        val threeDSSDK: Boolean,
        val threeDSServerTransId: String,
        val threeDSSDKEncData: String,
        val threeDSSDKEphemPubKey: String,
        val threeDSSDKAppId: String,
        val threeDSSDKTransId: String,
        val threeDSSDKReferenceNumber: String
    )

    data class PaymentFinishOrderRequest(
        val tDsTransId: String,
        val userName: String,
        val password: String
    )

    data class PaymentCheckOrderStatusRequest(
        val orderId: String,
        val userName: String,
        val password: String
    )

    @Serializable
    data class FinishOrderResponse(
        @SerialName("redirect")
        val redirect: String,
        @SerialName("errorCode")
        val errorCode: Int,
        @SerialName("is3DSVer2")
        val is3DSVer2: Boolean
    )

    @Serializable
    data class RegisterOrderResponse(
        @SerialName("orderId")
        val orderId: String
    )

    @Serializable
    data class PaymentOrderResponse(
        @SerialName("errorCode")
        val errorCode: Int,
        @SerialName("is3DSVer2")
        val is3DSVer2: Boolean,
        @SerialName("threeDSServerTransId")
        val threeDSServerTransId: String,
        @SerialName("threeDSSDKKey")
        val threeDSSDKKey: String
    )

    @Serializable
    data class PaymentOrderSecondStepResponse(
        @SerialName("info")
        val info: String,
        @SerialName("errorCode")
        val errorCode: Int,
        @SerialName("is3DSVer2")
        val is3DSVer2: Boolean,
        @SerialName("threeDSAcsTransactionId")
        val threeDSAcsTransactionId: String,
        @SerialName("threeDSAcsRefNumber")
        val threeDSAcsRefNumber: String,
        @SerialName("threeDSAcsSignedContent")
        val threeDSAcsSignedContent: String
    )
}