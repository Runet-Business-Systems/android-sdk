package ru.rbs.mobile.payment.sdk

import ru.rbs.mobile.payment.sdk.model.CameraScannerOptions
import ru.rbs.mobile.payment.sdk.model.Card
import ru.rbs.mobile.payment.sdk.model.CardSaveOptions
import ru.rbs.mobile.payment.sdk.model.HolderInputOptions
import ru.rbs.mobile.payment.sdk.model.PaymentConfig
import java.util.*

/**
 * Конструктор для формирования конфигурации оплаты.
 *
 * @param order идентификатор оплачиваемого заказа.
 */
class PaymentConfigBuilder(private val order: String) {
    private var buttonText: String? = null
    private var cards: Set<Card> = emptySet()
    private var cardSaveOptions: CardSaveOptions = CardSaveOptions.HIDE
    private var holderInputOptions: HolderInputOptions = HolderInputOptions.HIDE
    private var cameraScannerOptions: CameraScannerOptions = CameraScannerOptions.ENABLED
    private var uuid: String = UUID.randomUUID().toString()
    private var timestamp: Long = System.currentTimeMillis()
    private var locale: Locale = Locale.getDefault()
    private var bindingCVCRequired: Boolean = true

    /**
     * Изменение текста кнопки оплаты.
     *
     * Опционально, по умолчанию локализованный перевод "Оплатить"
     *
     * @param buttonText текст кнопки оплаты.
     * @return текущий конструктор.
     */
    fun buttonText(buttonText: String): PaymentConfigBuilder = apply {
        this.buttonText = buttonText
    }

    /**
     * Добавление списка связанных карт.
     *
     * Опционально, по умолчанию пустой список.
     *
     * @param cards список связанных карт.
     * @return текущий конструктор.
     */
    fun cards(cards: Set<Card>): PaymentConfigBuilder = apply {
        this.cards = cards
    }

    /**
     * Опция управления возможностью привязать новую карту.
     *
     * Опционально, по умолчанию HIDE
     *
     * @param options настройка функции привязки новой карты.
     * @return текущий конструктор.
     */
    fun cardSaveOptions(options: CardSaveOptions): PaymentConfigBuilder = apply {
        this.cardSaveOptions = options
    }

    /**
     * Опция управления функционалом сканирования карты.
     *
     * Опционально, по умолчанию ENABLED
     *
     * @param options настройка функции сканирования карты.
     * @return текущий конструктор.
     */
    fun cameraScannerOptions(options: CameraScannerOptions): PaymentConfigBuilder = apply {
        this.cameraScannerOptions = options
    }

    /**
     * Опция управления возможностью отображения поля ввода владельца карты.
     *
     * Опционально, по умолчанию HIDE
     *
     * @param options настройка поля ввода владельца карты.
     * @return текущий конструктор.
     */
    fun holderInputOptions(options: HolderInputOptions): PaymentConfigBuilder = apply {
        this.holderInputOptions = options
    }

    /**
     * Установка уникального идентификатора платежа.
     *
     * Опционально, уникальный идентификатор платежа, генерируется автоматически.
     *
     * @param uuid идентификатор платежа.
     * @return текущий конструктор.
     */
    fun uuid(uuid: String): PaymentConfigBuilder = apply {
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
    fun timestamp(timestamp: Long): PaymentConfigBuilder = apply {
        this.timestamp = timestamp
    }

    /**
     * Установка локализации.
     *
     * Опционально, локализация формы полаты, определяется автоматически.
     *
     * @param locale локализация.
     * @return текущий конструктор.
     */
    fun locale(locale: Locale): PaymentConfigBuilder = apply {
        this.locale = locale
    }

    /**
     * Установка проверки обязательного заполнения поля CVC при оплате привязанной картой.
     *
     * Опционально, по умолчанию true.
     *
     * @param required требование заполнения CVC.
     * @return текущий конструктор.
     */
    fun bindingCVCRequired(required: Boolean): PaymentConfigBuilder = apply {
        this.bindingCVCRequired = required
    }

    /**
     * Создает конфигурацию платежа.
     *
     * @return конфигурация платежа.
     */
    fun build() = PaymentConfig(
        order = this.order,
        cardSaveOptions = this.cardSaveOptions,
        holderInputOptions = this.holderInputOptions,
        cameraScannerOptions = this.cameraScannerOptions,
        cards = this.cards,
        uuid = this.uuid,
        timestamp = this.timestamp,
        buttonText = this.buttonText,
        locale = this.locale,
        bindingCVCRequired = this.bindingCVCRequired
    )
}
