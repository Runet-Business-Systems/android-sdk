package ru.rbs.mobile.payment.sdk.nfc

import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class NFCReadDelegateTest {

    private lateinit var nfcReadDelegate: NFCReadDelegate

    @MockK
    private lateinit var nfcAdapter: NfcAdapter

    @MockK
    private lateinit var activity: AppCompatActivity

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        nfcReadDelegate = NFCReadDelegate(nfcAdapter)
    }

    @Test
    fun `should call pause`() {
        every { nfcAdapter.disableForegroundDispatch(activity) } just runs

        nfcReadDelegate.onPause(activity)

        verify { nfcAdapter.disableForegroundDispatch(activity) }
    }

    @Test
    fun `should call is enabled`() {
        every { nfcAdapter.isEnabled } returns true

        nfcReadDelegate.isEnabled()

        verify { nfcAdapter.isEnabled }
    }
}
