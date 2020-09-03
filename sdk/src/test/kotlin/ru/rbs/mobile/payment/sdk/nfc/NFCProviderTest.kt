package ru.rbs.mobile.payment.sdk.nfc

import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.exception.CommunicationException
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.io.IOException

class NFCProviderTest {

    private lateinit var nfcProvider: NFCProvider

    @MockK
    private lateinit var isoDep: IsoDep

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        nfcProvider = NFCProvider(isoDep)
    }

    @Test
    fun `should call connect`() {
        every { isoDep.connect() } just runs

        nfcProvider.connect()

        verify { isoDep.connect() }
    }

    @Test(expected = CommunicationException::class)
    fun `should wrap internal exception`() {
        val array = byteArrayOf()
        every { isoDep.transceive(array) } throws IOException()

        nfcProvider.transceive(array)

        verify { isoDep.transceive(array) }
    }

    @Test
    fun `should call historical bytes`() {
        every { isoDep.historicalBytes } returns byteArrayOf()

        nfcProvider.at

        verify { isoDep.historicalBytes }
    }
}
