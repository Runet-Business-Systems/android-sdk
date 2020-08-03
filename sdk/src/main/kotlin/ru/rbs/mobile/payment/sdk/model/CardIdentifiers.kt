package ru.rbs.mobile.payment.sdk.model

import java.io.Serializable

/**
 * Идентификатор карты.
 */
interface CardIdentifier : Serializable {

    /**
     * Значение идентификатора.
     */
    val value: String
}

/**
 * Идентификатор по номеру карты.
 *
 * @param value номер карты.
 */
class CardPanIdentifier(override val value: String) : CardIdentifier

/**
 * Идентификатор по номеру привязки.
 *
 * @param value номер привязки.
 */
class CardBindingIdIdentifier(override val value: String) : CardIdentifier
