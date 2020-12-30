package ru.rbs.mobile.payment.sdk.ui.helper

import java.util.*

/**
 * Доступные локализации, в которых может работать функционал оплаты.
 */
object Locales {

    /**
     * Английский язык.
     */
    fun english(): Locale = Locale.ENGLISH

    /**
     * Русский язык.
     */
    fun russian(): Locale = Locale.Builder().setLanguage("ru").build()

    /**
     * Украинский язык.
     */
    fun ukrainian(): Locale = Locale.Builder().setLanguage("uk").build()

    /**
     * Немецкий язык.
     */
    fun german(): Locale = Locale.Builder().setLanguage("de").build()

    /**
     * Испанский язык.
     */
    fun spanish(): Locale = Locale.Builder().setLanguage("es").build()

    /**
     * Французский язык.
     */
    fun french(): Locale = Locale.Builder().setLanguage("fr").build()

    /**
     * Все доступные локализации формы оплаты.
     */
    fun availableLocales() = listOf(
        russian(),
        english(),
        ukrainian(),
        french(),
        spanish(),
        german()
    )
}
