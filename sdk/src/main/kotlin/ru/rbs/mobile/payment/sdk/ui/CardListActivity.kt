package ru.rbs.mobile.payment.sdk.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_card_list.*
import kotlinx.android.synthetic.main.activity_card_new.toolbar
import ru.rbs.mobile.payment.sdk.Constants
import ru.rbs.mobile.payment.sdk.R
import ru.rbs.mobile.payment.sdk.model.Card
import ru.rbs.mobile.payment.sdk.model.PaymentConfig
import ru.rbs.mobile.payment.sdk.ui.adapter.CardListAdapter

/**
 * Экран списка связанных карт.
 */
class CardListActivity : BaseActivity() {

    private val cardsAdapter = CardListAdapter()
    private val config: PaymentConfig by lazy {
        intent.getParcelableExtra<PaymentConfig>(Constants.INTENT_EXTRA_CONFIG) as PaymentConfig
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.rbs_title_card_list)
        }
        cardsAdapter.cards = config.cards.toList()
        cardList.apply {
            adapter = cardsAdapter
            layoutManager = LinearLayoutManager(this@CardListActivity)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
        doneButton.setOnClickListener {
            openNewCard()
        }
        cardsAdapter.cardSelectListener = object : CardListAdapter.CardSelectListener {
            override fun onCardSelected(card: Card) {
                openSavedCard(card)
            }
        }
    }

    private fun openNewCard() {
        startActivity(
            CardNewActivity.prepareIntent(this, config).also {
                it.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            }
        )
        finish()
    }

    private fun openSavedCard(card: Card) {
        startActivity(
            CardSelectedActivity.prepareIntent(
                this@CardListActivity,
                config,
                card
            ).also {
                it.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            }
        )
        finish()
    }

    companion object {

        /**
         * Подготавливает [Intent] для запуска экрана списка связанных карт.
         *
         * @param context для подготовки intent.
         * @param config конфигурация оплаты.
         */
        fun prepareIntent(
            context: Context,
            config: PaymentConfig
        ): Intent = Intent(context, CardListActivity::class.java).apply {
            putExtra(Constants.INTENT_EXTRA_CONFIG, config)
        }
    }
}
