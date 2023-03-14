package nithra.gift.suggestion.shop.birthday.marriage.notification

import android.content.Context
import android.net.ParseException
import org.json.JSONArray
import org.json.JSONObject
import android.os.Build
import nithra.gift.suggestion.shop.birthday.marriage.support.HttpHandler
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.android_id
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class.versioncode_get
import org.json.JSONException

object ServerUtilities {
    private var jArray: JSONArray? = null
    private var result: String? = null
    fun gcmpost(
        regId: String,
        Emailid: String?,
        vername: String?,
        vercode: Int,
        context: Context?
    ) {
        val sharedPreference = SharedPreference()
        val sh = HttpHandler()
        val url = "https://nithra.mobi/appgcm/gcmgiftsuggestion/register.php"
        val postDataParams = JSONObject()
        try {
            postDataParams.put("email", Emailid)
            postDataParams.put("regId", regId)
            postDataParams.put("vname", vername)
            postDataParams.put("vcode", vercode.toString() + "")
            postDataParams.put("andver", Build.VERSION.RELEASE)
            postDataParams.put("sw", "")
            postDataParams.put("asw", sharedPreference.getString(context!!, "smallestWidth"))
            postDataParams.put("w", sharedPreference.getString(context, "widthPixels"))
            postDataParams.put("h", sharedPreference.getString(context, "heightPixels"))
            postDataParams.put("d", sharedPreference.getString(context, "density"))
            println("regId1 : $regId")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        result = sh.makeServiceCall(url, postDataParams)
        println("response : " + result)
        try {
            println("ERROR----" + result)
            if (result == null) {
                println("ERROR----" + result + "1")
            } else {
                println("ERROR----" + result + "2")
                jArray = JSONArray(result)
                var json_data: JSONObject? = null
                var isvalided: Int
                println("result---/" + result)
                for (i in 0 until jArray!!.length()) {
                    json_data = jArray!!.getJSONObject(i)
                    isvalided = json_data.getInt("isvalid")
                    sharedPreference.putInt(context!!, "isvalid", isvalided)
                    sharedPreference.putInt(
                        context, "vcode", versioncode_get(
                            context
                        )
                    )
                    sharedPreference.putInt(
                        context, "fcm_update", versioncode_get(
                            context
                        )
                    )
                }
            }
        } catch (e1: JSONException) {
            println("ERROR----" + result + "3")
        } catch (e1: ParseException) {
            println("ERROR----" + result + "4")
        }
    }

    fun gcmupdate(context: Context?, vername: String?, vercode: Int, regid: String?) {
        val sharedPreference = SharedPreference()
        val sh = HttpHandler()
        val url = "https://nithra.mobi/appgcm/gcmgiftsuggestion/register.php"
        val postDataParams = JSONObject()
        try {
            postDataParams.put("vname", vername)
            postDataParams.put("vcode", Integer.toString(vercode))
            postDataParams.put("email", "" + android_id(context!!))
            postDataParams.put("regid", regid)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        result = sh.makeServiceCall(url, postDataParams)
        println("response : " + result)
        try {
            println("ERROR----" + result)
            if (result == null) {
                println("ERROR----" + result + "1")
            } else {
                println("ERROR----" + result + "2")
                jArray = JSONArray(result)
                var json_data: JSONObject? = null
                println("result---/" + result)
                for (i in 0 until jArray!!.length()) {
                    json_data = jArray!!.getJSONObject(i)
                    sharedPreference.putInt(
                        context!!, "fcm_update", versioncode_get(
                            context
                        )
                    )
                }
            }
        } catch (e1: JSONException) {
            println("ERROR----" + result + "3")
        } catch (e1: ParseException) {
            println("ERROR----" + result + "4")
        }
    }
}