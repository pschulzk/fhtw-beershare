package at.krutzler.beershare.webapi

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import at.krutzler.beershare.LoginActivity
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class WebApiClient(private val mNotAuthenticatedHandler: (() -> Unit)? = null) {

    object Constants {
        const val WEBAPI_TAG = "WebApi"
    }

    //private val baseUrl = "http://10.0.2.2:8000/api/v1"
    private val mBaseUrl = "http://192.168.1.7:8000/api/v1"

    fun get(path: String, callback: ((String, Boolean) -> Unit)? = null) {
        val url = URL("$mBaseUrl/$path")
        startRunnable(WebApiGetRunnable(url, callback))
    }

    fun post(path: String, data: String, callback: ((String, Boolean) -> Unit)? = null) {
        val url = URL("$mBaseUrl/$path")
        startRunnable(WebApiPostRunnable(url, data, callback))
    }

    private fun startRunnable(runnable: Runnable) {
        Thread(runnable).start()
    }

    private abstract inner class AWebApiRunnable(private val mCallback: ((String, Boolean) -> Unit)?)
        : Runnable {

        protected val mUsername = LoginActivity.username   // TODO
        protected val mPassword = LoginActivity.password   // TODO

        protected fun postResponse(response: String, error: Boolean) {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                // post to UI thread
                this.mCallback?.invoke(response, error)
            }
        }

        protected fun handleNotAuthenticated() {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                this@WebApiClient.mNotAuthenticatedHandler?.invoke()
            }
        }
    }

    private inner class WebApiGetRunnable(private val mUrl: URL,
                                          callback: ((String, Boolean) -> Unit)?)
        : AWebApiRunnable(callback) {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            try {
                with(mUrl.openConnection() as HttpURLConnection) {

                    // authorization
                    // TODO use token auth!
                    val message = "$mUsername:$mPassword".toByteArray(charset("UTF-8"))
                    val encoded: String = Base64.getEncoder().encodeToString(message)
                    setRequestProperty("Authorization", "Basic $encoded")
                    requestMethod = "GET"

                    Log.d(Constants.WEBAPI_TAG, "GET response code: $responseCode")
                    if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        handleNotAuthenticated();
                        return
                    }

                    val scanner = Scanner(inputStream)
                    scanner.useDelimiter("\\A")
                    if (scanner.hasNext()) {
                        val response = scanner.next()
                        postResponse(response, false)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                postResponse(String(), true)
            }
        }
    }

    private inner class WebApiPostRunnable(private val mUrl: URL,
                                           private val mData: String,
                                           callback: ((String, Boolean) -> Unit)?)
        : AWebApiRunnable(callback) {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            try {
                with(mUrl.openConnection() as HttpURLConnection) {

                    // authorization
                    // TODO use token auth!
                    val message = "$mUsername:$mPassword".toByteArray(charset("UTF-8"))
                    val encoded: String = Base64.getEncoder().encodeToString(message)
                    setRequestProperty("Authorization", "Basic $encoded")
                    setRequestProperty("Content-Type", "application/json; utf-8")
                    requestMethod = "POST"
                    doOutput = true

                    outputStream.use { os ->
                        val input: ByteArray = mData.toByteArray()
                        os.write(input, 0, input.size)
                    }

                    Log.d(Constants.WEBAPI_TAG, "POST response code: $responseCode")
                    if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        handleNotAuthenticated();
                        return
                    }

                    val scanner = Scanner(inputStream)
                    scanner.useDelimiter("\\A")
                    if (scanner.hasNext()) {
                        val response = scanner.next()
                        postResponse(response, false)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                postResponse(String(), true)
            }
        }
    }
}