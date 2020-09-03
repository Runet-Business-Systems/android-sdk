package ru.rbs.mobile.payment.sdk

import com.google.android.gms.wallet.PaymentDataRequest
import ru.rbs.mobile.payment.sdk.model.GooglePayPaymentConfig
import ru.rbs.mobile.payment.sdk.model.Theme
import java.util.*

/**
 * Конструктор для формирования конфигурации оплаты через кнопку Google Pay.
 *
 * @param order идентификатор оплачиваемого заказа.
 * @param paymentData информация для проведения платежа.
 */
@Suppress("TooManyFunctions")
class GooglePayConfigBuilder(
    private val order: String,
    private val paymentData: PaymentDataRequest
) {
    private var theme: Theme = Theme.SYSTEM
    private var locale: Locale = Locale.getDefault()
    private var uuid: String = UUID.randomUUID().toString()
    private var timestamp: Long = System.currentTimeMillis()
    private var testEnvironment: Boolean = false

    /**
     * Опция управления темой интерфейса.
     *
     * Опционально, по умолчанию SYSTEM.
     *
     * @param theme настройка функции сканирования карты.
     * @return текущий конструктор.
     */
    fun theme(theme: Theme): GooglePayConfigBuilder = apply {
        this.theme = theme
    }

    /**
     * Установка локализации.
     *
     * Опционально, локализация формы полаты, определяется автоматически.
     *
     * @param locale локализация.
     * @return текущий конструктор.
     */
    fun locale(locale: Locale): GooglePayConfigBuilder = apply {
        this.locale = locale
    }

    /**
     * Установка уникального идентификатора платежа.
     *
     * Опционально, уникальный идентификатор платежа, генерируется автоматически.
     *
     * @param uuid идентификатор платежа.
     * @return текущий конструктор.
     */
    fun uuid(uuid: String): GooglePayConfigBuilder = apply {
        this.uuid = uuid
    }

    /**
     * Установка времени формирования платежа.
     *
     * Опционально, время формирования платежа, устанавливается автоматически.
     *
     * @param timestamp время формирования платежа.
     * @return текущий конструктор.
     */
    fun timestamp(timestamp: Long): GooglePayConfigBuilder = apply {
        this.timestamp = timestamp
    }

    /**
     * Установка флага использования тестового окружения.
     *
     * Опционально, по умолчанию false.
     *
     * @param testEnvironment флаг использования тестового окружения.
     * @return текущий конструктор.
     */
    fun testEnvironment(testEnvironment: Boolean): GooglePayConfigBuilder = apply {
        this.testEnvironment = testEnvironment
    }

    /**
     * Создает конфигурацию платежа через кнопку Google Pay.
     *
     * @return конфигурация платежа.
     */
    fun build() = GooglePayPaymentConfig(
        order = this.order,
        paymentData = this.paymentData,
        uuid = this.uuid,
        theme = this.theme,
        locale = this.locale,
        timestamp = this.timestamp,
        testEnvironment = this.testEnvironment
    )
}
