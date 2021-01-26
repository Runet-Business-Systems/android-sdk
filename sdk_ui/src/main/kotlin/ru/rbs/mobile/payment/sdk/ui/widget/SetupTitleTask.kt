package ru.rbs.mobile.payment.sdk.ui.widget

import android.os.AsyncTask
import android.util.Log
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL


internal class SetupTitleTask(
    imageView: SVGImageView
) : AsyncTask<String?, Void?, SVG?>() {

    private val _imageView = WeakReference(imageView)

    @Suppress("TooGenericExceptionCaught")
    override fun doInBackground(vararg params: String?): SVG? {
        val urlToDisplay = params[0] ?: return null
        return try {
            val url = URL(urlToDisplay)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            if (urlConnection.contentLength > 0) {
                val inputStream: InputStream = urlConnection.inputStream
                SVG.getFromInputStream(inputStream)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("RBSSDK", e.message ?: e.toString())
            null
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override fun onPostExecute(result: SVG?) {
        _imageView.get()?.apply {
            if (result != null) {
                try {
                    setSVG(result)
                } catch (e: Exception) {
                    Log.e("RBSSDK", e.message ?: e.toString())
                }
            } else {
                setImageAsset("ic_bank_stub.svg")
            }
        }
    }
}
