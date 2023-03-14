package nithra.gift.suggestion.shop.birthday.marriage.support

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.net.*
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

class HttpHandler {
    var context: Context? = null

    constructor()
    constructor(context: Context?) {
        this.context = context
    }

    fun makeServiceCall(reqUrl: String?, postDataParams: JSONObject): String? {
        var response: String? = null
        try {
            val url = URL(reqUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true
            val os = conn.outputStream
            val writer = BufferedWriter(
                OutputStreamWriter(os, StandardCharsets.UTF_8)
            )
            writer.write(getPostDataString(postDataParams))
            writer.flush()
            writer.close()
            os.close()
            val responseCode = conn.responseCode
            return if (responseCode == HttpsURLConnection.HTTP_OK) {
                val `in` = BufferedReader(
                    InputStreamReader(
                        conn.inputStream
                    )
                )
                val sb = StringBuffer()
                var line: String? = ""
                while (`in`.readLine().also { line = it } != null) {
                    sb.append(line)
                    break
                }
                `in`.close()
                response = sb.toString()
                sb.toString()
            } else {
                "false : $responseCode"
            }
        } catch (e: MalformedURLException) {
            Log.e(TAG, "MalformedURLException: " + e.message)
        } catch (e: ProtocolException) {
            Log.e(TAG, "ProtocolException: " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "IOException: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
        return response
    }

    @Throws(Exception::class)
    fun getPostDataString(params: JSONObject): String {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
    }

    companion object {
        private val TAG = HttpHandler::class.java.simpleName
    }
}