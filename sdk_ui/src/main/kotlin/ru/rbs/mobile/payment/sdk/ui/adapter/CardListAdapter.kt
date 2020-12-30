package ru.rbs.mobile.payment.sdk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_bank_card.view.cardNumber
import kotlinx.android.synthetic.main.list_item_card_saved.view.*
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.model.Card
import ru.rbs.mobile.payment.sdk.ui.helper.CardLogoAssetsResolver
import ru.rbs.mobile.payment.sdk.ui.helper.CardNumberFormatter.maskCardNumber

internal class CardListAdapter : RecyclerView.Adapter<CardListAdapter.CardHolder>() {

    /**
     * Список карт для отображения.
     */
    var cards: List<Card> = emptyList()

    /**
     * Слушатель для отслеживания выбранной карты.
     */
    var cardSelectListener: CardSelectListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_card_saved, parent, false)
        )

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(cards[position])
    }

    inner class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Метод привязки данных [card] к UI элементу списка карт.
         *
         * @param card данные карты для отображения в списке.
         */
        fun bind(card: Card) {
            with(itemView) {
                cardNumber.text = maskCardNumber(itemView.context, card.pan)
                val logoResource = CardLogoAssetsResolver.resolveByPan(context, card.pan)
                if (logoResource != null) {
                    cardSystem.setImageAsset(logoResource)
                } else {
                    cardSystem.setImageDrawable(null)
                }
                setOnClickListener {
                    cardSelectListener?.onCardSelected(card)
                }
                if (card.expiryDate != null) {
                    cardExpiry.setExpiry(card.expiryDate)
                    cardExpiry.visibility = VISIBLE
                } else {
                    cardExpiry.visibility = INVISIBLE
                    cardExpiry.setExpiry("")
                }
            }
        }
    }

    /**
     * Интерфейс для определения выбранной карты.
     */
    interface CardSelectListener {

        /**
         * Вызывается при выборе карты пользователем.
         *
         * @param card выбранная карта.
         */
        fun onCardSelected(card: Card)
    }
}
