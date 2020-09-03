package ru.rbs.mobile.payment.sdk.nfc

import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.exception.CommunicationException
import com.github.devnied.emvnfccard.parser.IProvider
import java.io.IOException

/**
 * Провайдер для передачи команд чипу карточки.
 *
 * @param isoDep объект для передачи команд чипу карточки.
 */
class NFCProvider(private val isoDep: IsoDep) : IProvider {

    /**
     * Подключение.
     */
    fun connect() {
        isoDep.connect()
    }

    override fun transceive(pCommand: ByteArray?): ByteArray = try {
        isoDep.transceive(pCommand)
    } catch (e: IOException) {
        throw CommunicationException(e.message)
    }

    override fun getAt(): ByteArray = isoDep.historicalBytes
}
