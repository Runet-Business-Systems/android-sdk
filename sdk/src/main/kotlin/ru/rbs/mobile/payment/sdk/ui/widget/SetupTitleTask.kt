package ru.rbs.mobile.payment.sdk.ui.widget

import android.os.AsyncTask
import android.util.Log
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


internal class SetupTitleTask(
    private val image: SVGImageView
) : AsyncTask<String?, Void?, SVG?>() {

    @Suppress("TooGenericExceptionCaught")
    override fun doInBackground(vararg params: String?): SVG? {
        val urlToDisplay = params[0] ?: return null
        return try {
            val url = URL(urlToDisplay)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val inputStream: InputStream = urlConnection.inputStream
            SVG.getFromInputStream(inputStream)
        } catch (e: Exception) {
            Log.e("RBSSDK", e.message ?: e.toString())
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: SVG?) {
        if (result != null) {
            image.setSVG(result)
        } else {
            image.setImageAsset("ic_bank_stub.svg")
        }
    }
}
