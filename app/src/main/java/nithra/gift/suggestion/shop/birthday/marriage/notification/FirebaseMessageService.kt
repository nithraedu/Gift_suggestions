package nithra.gift.suggestion.shop.birthday.marriage.notification

import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import nithra.gift.suggestion.shop.birthday.marriage.support.SharedPreference
import nithra.gift.suggestion.shop.birthday.marriage.SplashScreen
import nithra.gift.suggestion.shop.birthday.marriage.support.Utils_Class
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.net.URLDecoder

class FirebaseMessageService : com.google.firebase.messaging.FirebaseMessagingService() {
    lateinit var myDB: SQLiteDatabase
    lateinit var sharedPreference: SharedPreference
    var iddd = 0
    lateinit private var noti: NotificationHelper
    var isclose = 0
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom())
        myDB = openOrCreateDatabase("myDB", 0, null)
        noti = NotificationHelper(this)
        val tablenew = "noti_cal"
        myDB.execSQL(
            "CREATE TABLE IF NOT EXISTS "
                    + tablenew + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR," +
                    "bm VARCHAR,ntype VARCHAR,url VARCHAR);"
        )
        sharedPreference = SharedPreference()
        if (remoteMessage.getData().size > 0) {
            try {
                Log.e("Data Payload: ", remoteMessage.getData().toString())
                val params: Map<String, String> = remoteMessage.getData()
                val `object` = JSONObject(params)
                Log.e("JSON_OBJECT", `object`.toString())
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(`object`)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Exception: ", e.message!!)
            }
        }
    }

    private fun handleDataMessage(data: JSONObject) {
        try {
            val message: String = data.getString("message")
            var title: String = data.getString("title")
            val date: String = data.getString("date")
            val time: String = data.getString("time")
            val type: String = data.getString("type")
            var bm: String = data.getString("bm")
            val ntype: String = data.getString("ntype")
            val url: String = data.getString("url")
            val pac: String = data.getString("pac")
            val intent_id = System.currentTimeMillis().toInt()
            if (sharedPreference!!.getString(
                    getApplicationContext(),
                    "old_msg"
                ) != message || sharedPreference!!.getString(
                    getApplicationContext(),
                    "old_tit"
                ) != title
            ) {
                sharedPreference!!.putString(getApplicationContext(), "old_msg", message)
                sharedPreference!!.putString(getApplicationContext(), "old_tit", title)
                try {
                    title = URLDecoder.decode(title, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                if (type == "s") {
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8")
                    } catch (e: UnsupportedEncodingException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    myDB.execSQL(
                        "INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                                "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','s','" + bm + "','" + ntype + "','" + url + "');"
                    )
                    sharedPreference!!.putInt(this, "typee", 0)
                    val c: Cursor = myDB.rawQuery("select id from noti_cal", null)
                    c.moveToLast()
                    iddd = c.getInt(0)
                    myDB.close()
                    if (ntype == "bt") {
                        noti.Notification_custom(
                            iddd,
                            title,
                            message,
                            url,
                            "bt",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    } else if (ntype == "bi") {
                        noti.Notification_custom(
                            iddd,
                            title,
                            message,
                            url,
                            "bi",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    } else {
                        noti.Notification_bm(
                            iddd,
                            title,
                            message,
                            url,
                            "bt",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    }
                } else if (type == "h") {
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8")
                    } catch (e: UnsupportedEncodingException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    if (ntype == "bt") {
                        noti.Notification_custom(
                            0, title, message, url, "bt", bm, sharedPreference
                                .getInt(this, "sund_chk1"), SplashScreen::class.java
                        )
                    } else if (ntype == "bi") {
                        noti.Notification_custom(
                            0, title, message, url, "bi", bm, sharedPreference
                                .getInt(this, "sund_chk1"), SplashScreen::class.java
                        )
                    }
                } else if (type == "st") {
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8")
                    } catch (e: UnsupportedEncodingException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    myDB.execSQL(
                        "INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                                "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','s','" + bm + "','" + ntype + "','" + url + "');"
                    )
                    sharedPreference!!.putInt(this, "typee", 0)
                    val c: Cursor = myDB.rawQuery("select id from noti_cal", null)
                    c.moveToLast()
                    iddd = c.getInt(0)
                    myDB.close()
                    /*if (sharedPreference.getBoolean(this, "notiS_onoff") == true) {*/if (ntype == "bt") {
                        noti.Notification_bm(
                            iddd,
                            title,
                            message,
                            url,
                            "bt",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    } else if (ntype == "bi") {
                        noti.Notification_bm(
                            iddd,
                            title,
                            message,
                            url,
                            "bi",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    } else {
                        noti.Notification_bm(
                            iddd,
                            title,
                            message,
                            url,
                            "bt",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    }
                    /*}*/
                } else if (type == "w") {
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8")
                    } catch (e: UnsupportedEncodingException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    myDB.execSQL(
                        "INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                                "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','w','" + bm + "','" + ntype + "','" + url + "');"
                    )
                    sharedPreference!!.putInt(this, "typee", 0)
                    val c: Cursor = myDB.rawQuery("select id from noti_cal", null)
                    c.moveToLast()
                    iddd = c.getInt(0)
                    myDB.close()
                    if (ntype == "bt") {
                        noti.Notification_custom(
                            iddd,
                            title,
                            message,
                            url,
                            "bt",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    } else if (ntype == "bi") {
                        noti.Notification_custom(
                            iddd,
                            title,
                            message,
                            url,
                            "bi",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    } else {
                        noti.Notification_bm(
                            iddd,
                            title,
                            message,
                            url,
                            "bt",
                            bm,
                            sharedPreference!!.getInt(this, "sund_chk1"),
                            ST_Activity::class.java
                        )
                    }
                } else if (type == "ns") {
                    myDB.execSQL(
                        "INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                                "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','ns','" + bm + "','" + ntype + "','" + url + "');"
                    )
                    val c: Cursor = myDB.rawQuery("select id from noti_cal", null)
                    c.moveToLast()
                    iddd = c.getInt(0)
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8")
                    } catch (e: UnsupportedEncodingException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    myDB.close()
                } else if (type == "ins") {
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8")
                    } catch (e: UnsupportedEncodingException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    /*if (sharedPreference.getBoolean(this, "notiS_onoff") == true) {*/noti.Notification1(
                        intent_id,
                        title,
                        message,
                        url,
                        "bi",
                        bm,
                        sharedPreference!!.getInt(this, "sund_chk1"),
                        ST_Activity::class.java
                    )
                    /*}*/
                } else if (type == "ap") {
                    if (!appInstalledOrNot(pac)) {
                        if (ntype == "n") {
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8")
                            } catch (e: UnsupportedEncodingException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            }
                            noti.Notification1(
                                intent_id,
                                title,
                                message,
                                url,
                                "bt",
                                bm,
                                sharedPreference!!.getInt(this, "sund_chk1"),
                                ST_Activity::class.java
                            )
                        } else if (ntype == "bt") {
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8")
                            } catch (e: UnsupportedEncodingException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            }
                            noti.Notification1(
                                intent_id,
                                title,
                                message,
                                url,
                                "bt",
                                bm,
                                sharedPreference!!.getInt(this, "sund_chk1"),
                                ST_Activity::class.java
                            )
                        } else if (ntype == "bi") {
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8")
                            } catch (e: UnsupportedEncodingException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            }
                            noti.Notification1(
                                intent_id,
                                title,
                                message,
                                url,
                                "bi",
                                bm,
                                sharedPreference!!.getInt(this, "sund_chk1"),
                                ST_Activity::class.java
                            )
                        } else if (ntype == "w") {
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8")
                            } catch (e: UnsupportedEncodingException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            }
                            noti.Notification1(
                                intent_id,
                                title,
                                message,
                                url,
                                "bi",
                                bm,
                                sharedPreference!!.getInt(this, "sund_chk1"),
                                ST_Activity::class.java
                            )
                        }
                    }
                } else if (type == "u") {
                    sharedPreference!!.putInt(this, "gcmvcode", message.toInt())
                    sharedPreference!!.putInt(this, "isvupdate", 1)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm: PackageManager = getPackageManager()
        var app_installed = false
        app_installed = try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
        return app_installed
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        sendRegistrationToServer(s)
    }

    private fun sendRegistrationToServer(token: String) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: $token")
        println("sendRegistrationToServer: $token")
        val sharedPreference = SharedPreference()
        if (sharedPreference.getString(this, "token") != token) {
            sharedPreference.putString(this, "token", token)
            if (sharedPreference.getInt(this@FirebaseMessageService, "isvalid") == 0) {
                if (sharedPreference.getString(this@FirebaseMessageService, "token")!!.length > 0) {
                    ServerUtilities.gcmpost(
                        token,
                        Utils_Class.androidId(this@FirebaseMessageService),
                        Utils_Class.versionname_get(this@FirebaseMessageService),
                        Utils_Class.versioncode_get(this@FirebaseMessageService),
                        this@FirebaseMessageService
                    )
                    sharedPreference.putInt(
                        this@FirebaseMessageService,
                        "fcm_update",
                        Utils_Class.versioncode_get(this@FirebaseMessageService)
                    )
                }
            } else if (sharedPreference.getInt(
                    this@FirebaseMessageService,
                    "fcm_update"
                ) < Utils_Class.versioncode_get(this@FirebaseMessageService)
            ) {
                ServerUtilities.gcmupdate(
                    this@FirebaseMessageService,
                    Utils_Class.versionname_get(this@FirebaseMessageService),
                    Utils_Class.versioncode_get(this@FirebaseMessageService),
                    token
                )
                sharedPreference.putInt(
                    this@FirebaseMessageService,
                    "fcm_update",
                    Utils_Class.versioncode_get(this@FirebaseMessageService)
                )
            }
        }
    }

    companion object {
        private val TAG = FirebaseMessageService::class.java.simpleName
    }
}