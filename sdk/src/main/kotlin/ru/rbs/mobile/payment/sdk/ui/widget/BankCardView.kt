package ru.rbs.mobile.payment.sdk.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.getColor
import kotlinx.android.synthetic.main.layout_bank_card.view.*
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.model.ExpiryDate
import ru.rbs.mobile.payment.sdk.ui.helper.CardLogoAssetsResolver
import ru.rbs.mobile.payment.sdk.utils.noSpaces
import ru.rbs.mobile.payment.sdk.utils.parseColor
import kotlin.math.min


/**
 * UI компонент для отображения данных банковской карты.
 */
@Suppress("TooManyFunctions")
class BankCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var gradientDrawable: GradientDrawable

    init {
        inflate(context, R.layout.layout_bank_card, this)
        initBackground()
        setupEmptyStyle()
        setNumber("")
        setExpiry("")
    }

    /**
     * Установка срока действия карты.
     *
     * @param expiry строка срока действия карты в формате "MM/YY".
     */
    fun setExpiry(expiry: String) {
        cardExpiry.setExpiry(expiry)
    }

    /**
     * Установка срока действия карты.
     *
     * @param expiry срок действия карты.
     */
    fun setExpiry(expiry: ExpiryDate) {
        cardExpiry.setExpiry(expiry)
    }

    private fun initBackground() {
        val cornerRadius = resources.getDimension(R.dimen.rbs_card_corner_radius)
        val elevation = resources.getDimension(R.dimen.rbs_card_elevation).toInt()
        val shadowColor = getColor(context, R.color.rbs_color_shadow)
        val outerRadius = floatArrayOf(
            cornerRadius, cornerRadius, cornerRadius, cornerRadius,
            cornerRadius, cornerRadius, cornerRadius, cornerRadius
        )

        val shapeDrawablePadding = Rect().apply {
            left = elevation
            right = elevation
            top = elevation * 2
            bottom = elevation * 2
        }

        val dy = elevation / SHADOW_DIVIDER

        gradientDrawable = GradientDrawable().also {
            it.orientation = GradientDrawable.Orientation.TL_BR
            it.cornerRadius = cornerRadius
        }

        val shapeDrawable = ShapeDrawable().apply {
            setPadding(shapeDrawablePadding)
            paint.color = Color.TRANSPARENT
            paint.setShadowLayer(
                cornerRadius / 2,
                0f,
                dy.toFloat(),
                shadowColor
            )
            shape = RoundRectShape(outerRadius, null, null)
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val drawable = LayerDrawable(arrayOf(shapeDrawable, gradientDrawable))
        drawable.setLayerInset(0, elevation, elevation * 2, elevation, elevation * 2)
        background = drawable
    }

    /**
     * Установка номера карты.
     *
     * @param number номер карты.
     */
    fun setNumber(number: String) {
        val clearNumber = number.noSpaces(NUMBER_MAX_LENGTH)
            .replace("[^\\d.]".toRegex(), "•")
        val formatted = StringBuilder(NUMBER_MASK)
        formatted.replace(0, clearNumber.length, clearNumber)
        for (position in SPACE_POSITIONS) {
            if (formatted.length > position) {
                formatted.insert(position, SPACE).toString()
            }
        }
        cardNumber.text = formatted
    }

    /**
     * Настройка отображения поля срока действия карты.
     *
     * @param enabled если true, то поле отображается, в противном случае нет.
     */
    fun enableExpiry(enabled: Boolean) {
        cardExpiry.visibility = if (enabled) View.VISIBLE else View.INVISIBLE
    }

    /**
     * Настройка отображения поля владельца карты.
     *
     * @param enabled если true, то поле отображается, в противном случае нет.
     */
    fun enableHolderName(enabled: Boolean) {
        cardHolder.visibility = if (enabled) View.VISIBLE else View.INVISIBLE
    }

    /**
     * Установка имени владельца карты.
     *
     * @param name имя владельца карты.
     */
    fun setHolderName(name: String) {
        cardHolder.text = name
    }

    /**
     * Установка ссылки на логотип банка.
     *
     * @param url ссылка на логотип.
     */
    fun setBankLogoUrl(url: String?) {
        SetupTitleTask(cardBankLogo).execute(url)
    }

    /**
     * Установка типа платежной системы.
     *
     * При установке [preferLight] в значение true, в первую очередь будет искаться подходящий
     * светлый логотип платежной системы.
     *
     * @param system название платежной системы.
     * @param preferLight предпочтение использования светового логотипа платежной системы.
     */
    fun setPaymentSystem(system: String, preferLight: Boolean = false) {
        val logoResource = CardLogoAssetsResolver.resolveByName(context, system, preferLight)
        if (logoResource != null) {
            cardSystem.setImageAsset(logoResource)
        } else {
            cardSystem.setImageDrawable(null)
        }
    }

    /**
     * Установка цвета текста полей на карте.
     *
     * @param color цвет в формате "#ffffff" или в сокращенном варианте "#fff".
     */
    fun setTextColor(color: String) = setTextColor(color.parseColor())

    /**
     * Установка цвета текста полей на карте.
     *
     * @param color цвет.
     */
    fun setTextColor(color: Int) {
        cardHolder.setTextColor(color)
        cardHolder.setHintTextColor(color)
        cardNumber.setTextColor(color)
        cardNumber.setHintTextColor(color)
        cardExpiry.setTextColor(color)
        cardExpiry.setHintTextColor(color)
    }

    /**
     * Установка градиента фона карты.
     *
     * @param startColor начальный цвет градиента, в формате "#ffffff" или в сокращенном варианте
     * "#fff".
     * @param endColor конечный цвет градиента, в формате "#ffffff" или в сокращенном варианте
     * "#fff".
     */
    fun setBackground(startColor: String, endColor: String) =
        setBackground(
            startColor.parseColor(),
            endColor.parseColor()
        )

    /**
     * Установка градиента фона карты.
     *
     * @param startColor начальный цвет градиента.
     * @param endColor конечный цвет градиента.
     */
    fun setBackground(startColor: Int, endColor: Int) {
        gradientDrawable.colors = intArrayOf(startColor, endColor)
        drawableStateChanged()
    }

    /**
     * Установка стиля карты для неизвестного банка.
     */
    fun setupUnknownBrand() {
        val color = getColor(context, R.color.rbs_color_card_background)
        setBackground(color, color)
        setTextColor(getColor(context, R.color.rbs_color_text))
        setPaymentSystem("")
        setBankLogoUrl(null)
    }

    private fun setupEmptyStyle() {
        val color = getColor(context, R.color.rbs_color_card_background)
        setBackground(color, color)
        setTextColor(getColor(context, R.color.rbs_color_text))
        setPaymentSystem("")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val params = layoutParams
        val formMargin = if (params is MarginLayoutParams) {
            params.leftMargin + params.rightMargin
        } else {
            0
        }
        val size = min(
            MeasureSpec.getSize(widthMeasureSpec) - formMargin,
            resources.getDimensionPixelSize(R.dimen.rbs_card_max_width) - formMargin
        )
        val widthSpec = MeasureSpec.makeMeasureSpec(size, EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec((size / CARD_RATIO).toInt(), EXACTLY)
        super.onMeasure(widthSpec, heightSpec)
    }

    companion object {
        private const val SHADOW_DIVIDER = 3
        private const val CARD_RATIO = 1.50f
        private const val SPACE = " "
        private const val NUMBER_MASK = "••••••••••••••••  "
        private const val NUMBER_MAX_LENGTH = 19
        private val SPACE_POSITIONS = intArrayOf(4, 9, 14)
    }
}
