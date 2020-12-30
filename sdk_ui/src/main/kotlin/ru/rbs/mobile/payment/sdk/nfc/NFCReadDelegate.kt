package ru.rbs.mobile.payment.sdk.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.parser.EmvTemplate
import java.util.*

/**
 * Сканер данных с карточки.
 *
 * @param nfcAdapter адаптер для взаимодействия с NFC.
 */
class NFCReadDelegate(private val nfcAdapter: NfcAdapter) {

    /**
     * Слушатель для отслеживания процесса считывания данных карты по NFC.
     */
    var nfcCardListener: NFCCardListener? = null

    /**
     * Необходимо вызывать в методе onNewIntent Activity, где обрабатываются данные от NFC.
     *
     * @param intent намерение с данным от NFC чипа.
     * @return true если [intent] соответствует данным от NFC чипа, в противном случае false.
     */
    @Suppress("TooGenericExceptionCaught")
    fun onNewIntent(intent: Intent): Boolean {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
            || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
            || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action
        ) {
            try {
                val iTag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                val provider = NFCProvider(IsoDep.get(iTag))
                val config: EmvTemplate.Config = EmvTemplate.Config()
                    .setContactLess(true)
                    .setReadAllAids(false)
                    .setReadTransactions(false)
                    .setRemoveDefaultParsers(false)
                    .setReadAt(false)
                val parser = EmvTemplate.Builder()
                    .setProvider(provider)
                    .setConfig(config)
                    .build()

                provider.connect()
                parser.readEmvCard()?.let { card ->
                    nfcCardListener?.onCardReadSuccess(
                        number = card.cardNumber,
                        expiryDate = card.expireDate
                    )
                }
            } catch (e: Exception) {
                nfcCardListener?.onCardReadError(e)
            }
            return true
        } else {
            return false
        }
    }

    /**
     * Возвращает статус работы NFC.
     *
     * @return true если NFC включен на устройстве, в противном случае false.
     */
    fun isEnabled(): Boolean = nfcAdapter.isEnabled

    /**
     * Необходимо вызывать в методе onResume Activity, где обрабатываются данные от NFC.
     *
     * @param activity экран обработки данных от NFC.
     * @param activityClass класс Activity экрана.
     */
    fun onResume(activity: Activity, activityClass: Class<*>) {
        val updateIntent = Intent(activity, activityClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(activity, 0, updateIntent, 0)
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, arrayOf<IntentFilter>(), null)
    }

    /**
     * Необходимо вызывать в методе onPause Activity, где обрабатываются данные от NFC.
     *
     * @param activity экран обработки данных от NFC.
     */
    fun onPause(activity: Activity) {
        nfcAdapter.disableForegroundDispatch(activity)
    }

    /**
     * Интерфейс для отслеживания состояния считывания данных карты.
     */
    interface NFCCardListener {

        /**
         * Вызывается в случае успешного считывания данных карты.
         *
         * @param number номер карты.
         * @param expiryDate срок действия карты.
         */
        fun onCardReadSuccess(number: String, expiryDate: Date?)

        /**
         * Вызывается в случае возникновения ошибки в процессе считывания карты.
         *
         * @param e возникшая ошибка.
         */
        fun onCardReadError(e: Exception)
    }
}
