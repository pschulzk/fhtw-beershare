package at.krutzler.beershare.webapi

import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import at.krutzler.beershare.LoginActivity
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

class WebApiClient(private val mNotAuthenticatedHandler: (() -> Unit)? = null) {

    companion object {
        private const val TAG = "WebApi"
        var backendHostname = "0.0.0.0:8000"
    }

    fun get(path: String, callback: ((String, Boolean) -> Unit)? = null) {
        val url = URL("${baseUrl()}/$path")
        startRunnable(WebApiGetRunnable(url, "GET", callback))
    }

    fun post(path: String, data: String, callback: ((String, Boolean) -> Unit)? = null) {
        val url = URL("${baseUrl()}/$path")
        startRunnable(WebApiPostRunnable(url, "POST", data, callback))
    }

    fun put(path: String, data: String, callback: ((String, Boolean) -> Unit)? = null) {
        val url = URL("${baseUrl()}/$path")
        startRunnable(WebApiPostRunnable(url, "PUT", data, callback))
    }

    fun delete(path: String, callback: ((String, Boolean) -> Unit)? = null) {
        val url = URL("${baseUrl()}/$path")
        startRunnable(WebApiGetRunnable(url, "DELETE", callback))
    }

    private fun baseUrl(): String {
        return "http://$backendHostname/api/v1"
    }

    private fun startRunnable(runnable: Runnable) {
        Thread(runnable).start()
    }

    private abstract inner class AWebApiRunnable(protected val mUrl: URL,
                                                 protected val mRequestMethod: String,
                                                 private val mCallback: ((String, Boolean) -> Unit)?)
        : Runnable {

        protected val mUsername = LoginActivity.username   // TODO
        protected val mPassword = LoginActivity.password   // TODO

        protected fun postResponse(response: String, error: Boolean) {
            Handler(Looper.getMainLooper()).post {
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

    private inner class WebApiGetRunnable(url: URL,
                                          requestMethod: String,
                                          callback: ((String, Boolean) -> Unit)?)
        : AWebApiRunnable(url, requestMethod, callback) {

        override fun run() {
            try {
                with(mUrl.openConnection() as HttpURLConnection) {

                    // authorization
                    // TODO use token auth!
                    val message = "$mUsername:$mPassword".toByteArray(charset("UTF-8"))
                    val encoded: String = Base64.encodeToString(message, Base64.DEFAULT)
                    setRequestProperty("Authorization", "Basic $encoded")
                    requestMethod = mRequestMethod
                    connectTimeout = 5 * 1000  // 5s timeout

                    Log.d(TAG, "GET response code: $responseCode")
                    if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        handleNotAuthenticated()
                        return
                    }

                    if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                        postResponse("", false)
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

    private inner class WebApiPostRunnable(url: URL,
                                           requestMethod: String,
                                           private val mData: String,
                                           callback: ((String, Boolean) -> Unit)?)
        : AWebApiRunnable(url, requestMethod, callback) {

        override fun run() {
            try {
                with(mUrl.openConnection() as HttpURLConnection) {

                    // authorization
                    // TODO use token auth!
                    val message = "$mUsername:$mPassword".toByteArray(charset("UTF-8"))
                    val encoded: String = Base64.encodeToString(message, Base64.DEFAULT)
                    setRequestProperty("Authorization", "Basic $encoded")
                    setRequestProperty("Content-Type", "application/json; utf-8")
                    requestMethod = mRequestMethod
                    doOutput = true

                    outputStream.use { os ->
                        val input: ByteArray = mData.toByteArray()
                        os.write(input, 0, input.size)
                    }

                    Log.d(TAG, "POST response code: $responseCode")
                    if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        handleNotAuthenticated()
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