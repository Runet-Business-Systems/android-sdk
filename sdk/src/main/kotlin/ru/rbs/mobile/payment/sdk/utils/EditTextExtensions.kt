package ru.rbs.mobile.payment.sdk.utils

import android.view.MotionEvent
import android.widget.EditText
import androidx.core.content.ContextCompat

/**
 * Установка иконки и обработчика нажатия на иконку в правую часть поля ввода.
 *
 * @param resId идентификатор графического ресурса.
 * @param onClicked обработчик нажатия.
 */
fun EditText.addRightButton(resId: Int, onClicked: (view: EditText) -> Unit) {
    addRightDrawable(resId)
    onRightDrawableClicked(onClicked)
}

/**
 * Расширение для удаления иконки и слушателя по нажатию на иконку в правой части поля ввода.
 */
fun EditText.clearRightButton() {
    addRightDrawable(null)
    onRightDrawableClicked(null)
}

/**
 * Расширение для установки иконки в правой части поля ввода.
 *
 * @param resId идентификатор графического ресурса.
 */
fun EditText.addRightDrawable(resId: Int?) {
    val icon = resId?.let { ContextCompat.getDrawable(context, it) }
    icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
    setCompoundDrawables(null, null, icon, null)
}

/**
 * Расширение для установки обработчика нажатия по иконке в правой части поля ввода
 *
 * @param onClicked обработчик нажатия.
 */
fun EditText.onRightDrawableClicked(onClicked: ((view: EditText) -> Unit)?) {
    if (onClicked == null) {
        this.setOnTouchListener(null)
    } else {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }
}
