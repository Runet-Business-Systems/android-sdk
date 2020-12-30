package ru.rbs.mobile.payment.sdk.utils

import android.os.Parcel

/**
 * Расширение для чтения значения Boolean из Parcel.
 *
 * Для получения значения Bool используется метод Parcel.readByte().
 *
 * @return true если считано значение 1, в противном случае false.
 */
fun Parcel.readBooleanValue(): Boolean = readByte() == 1.toByte()

/**
 * Расширение для записи значения Boolean в Parcel.
 *
 * Для записи значения Bool используется метод Parcel.writeByte(). Для записи true используется
 * значение 1, для записи false значение 0.
 *
 * @param value значение Boolean для записи в Parcel.
 */
fun Parcel.writeBooleanValue(value: Boolean) {
    writeByte(if (value) 1 else 0)
}
