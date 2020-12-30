package ru.rbs.mobile.payment.sdk.core.utils

import android.graphics.Color
import ru.rbs.mobile.payment.sdk.core.model.ExpiryDate
import java.util.*

/**
 * Возвращает содержание pem файла в виде строки.
 *
 * @return содержание pem файла.
 */
fun String.pemKeyContent(): String =
    replace("\\s+", "")
        .replace("\n", "")
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")

/**
 * Расширение для получения цвета из строкового представления формата #ffffff или его сокращенного
 * вида #fff.
 *
 * @return цвет полученный из строкового представления.
 */
@Suppress("MagicNumber")
fun String.parseColor(): Int = Color.parseColor(
    if (this.length == 4) {
        "#${this[1]}${this[1]}${this[2]}${this[2]}${this[3]}${this[3]}"
    } else {
        this
    }
)

/**
 * Расширение для получения сроки состоящей только из цифр, все прочие символы буду удалены.
 *
 * Если задан параметр [maxLength], то строка из цифр будет обрезана до указанной длины.
 *
 * @param maxLength максимальная длина строки.
 * @return обработанную сроку.
 */
fun String.digitsOnly(maxLength: Int? = null) = run {
    val digits = replace("[^\\d.]".toRegex(), "")
    if (maxLength != null) {
        digits.take(maxLength)
    } else {
        digits
    }
}

/**
 * Расширение для получения сроки без пробелов, все пробелы будут удалены.
 *
 * Если задан параметр [maxLength], то строка без пробелов будет обрезана до указанной длины.
 *
 * @param maxLength максимальная длина строки.
 * @return обработанную сроку.
 */
fun String.noSpaces(maxLength: Int? = null) = run {
    val digits = replace("[\\s.]".toRegex(), "")
    if (maxLength != null) {
        digits.take(maxLength)
    } else {
        digits
    }
}

/**
 * Возвращает объект [ExpiryDate] построенный на строке формата MM/YY.
 *
 * @return информацию о сроке действия карты.
 */
@Suppress("MagicNumber")
fun String.toExpDate(): ExpiryDate {
    if (!matches("\\d{2}/\\d{2}".toRegex())) {
        throw IllegalArgumentException("Incorrect format, should be MM/YY.")
    }
    return ExpiryDate(
        expMonth = substring(0, 2).toInt(),
        expYear = substring(3, 5).toInt() + 2000
    )
}

/**
 * Преобразует дату в сроку формата MM/YY.
 *
 * @return информацию о сроке действия карты.
 */
@Suppress("MagicNumber")
fun Date.toStringExpDate(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    val month = (calendar.get(Calendar.MONTH) + 1).toString()
        .padStart(2, '0')
    val year = calendar.get(Calendar.YEAR) % 100
    return "$month/$year"
}
