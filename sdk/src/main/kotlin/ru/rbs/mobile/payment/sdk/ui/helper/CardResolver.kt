package ru.rbs.mobile.payment.sdk.ui.helper

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rbs.mobile.payment.sdk.component.CardInfoProvider
import ru.rbs.mobile.payment.sdk.ui.widget.BankCardView
import ru.rbs.mobile.payment.sdk.utils.digitsOnly

internal class CardResolver(
    private val bankCardView: BankCardView,
    private val cardInfoProvider: CardInfoProvider
) {
    private var prevBin: String = ""

    @Suppress("TooGenericExceptionCaught")
    fun resolve(number: String) {
        val clearNumber = number.digitsOnly()
        if (clearNumber.length < prevBin.length) {
            prevBin = ""
            GlobalScope.launch(Dispatchers.Main) {
                with(bankCardView) {
                    setupUnknownBrand()
                }
            }
        }
        val hasMinLength = clearNumber.length >= CARD_BIN_MIN_LENGTH
        if (hasMinLength) {
            val bin = clearNumber.substring(0, clearNumber.length.coerceAtMost(CARD_BIN_MAX_LENGTH))
            if (bin != prevBin) {
                GlobalScope.launch(Dispatchers.Main) {
                    with(bankCardView) {
                        val info = try {
                            withContext(Dispatchers.IO) { cardInfoProvider.resolve(bin) }
                        } catch (e: Exception) {
                            Log.e("RBSSDK", e.message)
                            null
                        }
                        info?.let {
                            setTextColor(info.textColor)
                            if (info.backgroundGradient.size >= 2) {
                                setBackground(
                                    info.backgroundGradient[0],
                                    info.backgroundGradient[1]
                                )
                            } else {
                                setBackground(info.backgroundColor, info.backgroundColor)
                            }
                            if (info.backgroundLightness) {
                                setBankLogoUrl(info.logo)
                                setPaymentSystem(info.paymentSystem, false)
                            } else {
                                setBankLogoUrl(info.logoInvert)
                                setPaymentSystem(info.paymentSystem, true)
                            }
                        }
                    }
                }
            }
            prevBin = bin
        }
    }

    companion object {
        private const val CARD_BIN_MIN_LENGTH = 6
        private const val CARD_BIN_MAX_LENGTH = 8
    }
}
