package ru.rbs.mobile.payment.sdk.utils

import android.graphics.drawable.LayerDrawable
import android.view.MotionEvent
import android.widget.EditText
import androidx.core.content.ContextCompat
import ru.rbs.mobile.payment.sdk.R

/**
 * Установка иконки и обработчика нажатия на иконку в правую часть поля ввода.
 *
 * @param buttons описание кнопок.
 */
fun EditText.addRightButtons(buttons: List<Pair<Int, () -> Unit>>) {
    addRightDrawables(buttons.map { it.first })
    onRightDrawablesClicked(buttons.map { it.second })
}

/**
 * Расширение для установки иконки в правой части поля ввода.
 *
 * @param resIds идентификаторы графических ресурсов.
 */
fun EditText.addRightDrawables(resIds: List<Int>) {
    if (resIds.isEmpty()) {
        setCompoundDrawables(null, null, null, null)
    } else {
        val padding = resources.getDimensionPixelSize(R.dimen.rbs_edit_text_icons_padding)
        val icons = resIds.map { ContextCompat.getDrawable(context, it)!! }
        val width = icons.sumBy { it.intrinsicWidth } + ((icons.size - 1) * padding)
        val height = icons.maxBy { it.intrinsicHeight }!!.intrinsicHeight
        val dl = LayerDrawable(icons.toTypedArray())
        dl.setBounds(0, 0, width, height)
        icons.fold(0, { acc, icon ->
            val shift = acc + icon.intrinsicWidth
            val top = (height - icon.intrinsicHeight) / 2
            icon.setBounds(acc, top, shift, icon.intrinsicHeight)
            shift + padding
        })
        setCompoundDrawables(null, null, dl, null)
    }
}

/**
 * Расширение для установки обработчика нажатия по иконке в правой части поля ввода
 *
 * @param onClicked обработчик нажатия.
 */
fun EditText.onRightDrawablesClicked(onClicked: List<(() -> Unit)>) {
    if (onClicked.isEmpty()) {
        this.setOnTouchListener(null)
    } else {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                val x = event.x
                val drawableLeft = v.width - v.totalPaddingRight
                val rightDrawable = v.compoundDrawables[2]
                if (x >= drawableLeft &&
                    event.action == MotionEvent.ACTION_UP &&
                    rightDrawable is LayerDrawable
                ) {
                    for (iconIndex in 0 until rightDrawable.numberOfLayers) {
                        val icon = rightDrawable.getDrawable(iconIndex)
                        val leftBorder = drawableLeft + icon.bounds.left
                        val rightBorder = drawableLeft + icon.bounds.right
                        if (x >= leftBorder && x <= rightBorder) {
                            onClicked[iconIndex]()
                            break
                        }
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }
}
