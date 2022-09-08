package nithra.gift.suggestion.shop.birthday.marriage.Notification;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import nithra.gift.suggestion.shop.birthday.marriage.MainActivity;
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference;


public class FirebaseMessageService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = FirebaseMessageService.class.getSimpleName();
    SQLiteDatabase myDB;
    SharedPreference sharedPreference;
    int iddd;
    private NotificationHelper noti;
    int isclose = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        myDB = openOrCreateDatabase("myDB", 0, null);
        noti = new NotificationHelper(this);
        String tablenew = "noti_cal";
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + tablenew + " (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,title VARCHAR,message VARCHAR,date VARCHAR,time VARCHAR,isclose INT(4),isshow INT(4) default 0,type VARCHAR," +
                "bm VARCHAR,ntype VARCHAR,url VARCHAR);");
        sharedPreference = new SharedPreference();
        if (remoteMessage.getData().size() > 0) {
            try {
                Log.e("Data Payload: ", remoteMessage.getData().toString());
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);

                Log.e("JSON_OBJECT", object.toString());
                //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(object);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception: ", e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject data) {
        try {
            String message = data.getString("message");
            String title = data.getString("title");
            String date = data.getString("date");
            String time = data.getString("time");
            String type = data.getString("type");
            String bm = data.getString("bm");
            String ntype = data.getString("ntype");
            String url = data.getString("url");
            String pac = data.getString("pac");

            int intent_id = (int) System.currentTimeMillis();
            if (!sharedPreference.getString(getApplicationContext(), "old_msg")
                    .equals(message) || !sharedPreference.getString(getApplicationContext(), "old_tit")
                    .equals(title)) {
                sharedPreference.putString(getApplicationContext(), "old_msg", message);
                sharedPreference.putString(getApplicationContext(), "old_tit", title);

                try {
                    title = URLDecoder.decode(title, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (type.equals("s")) {
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    myDB.execSQL("INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                            "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','s','" + bm + "','" + ntype + "','" + url + "');");
                    sharedPreference.putInt(this, "typee", 0);
                    Cursor c = myDB.rawQuery("select id from noti_cal", null);
                    c.moveToLast();
                    iddd = c.getInt(0);
                    myDB.close();
                    if (ntype.equals("bt")) {
                        noti.Notification_custom(iddd, title, message, url, "bt", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    } else if (ntype.equals("bi")) {
                        noti.Notification_custom(iddd, title, message, url, "bi", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    } else {
                        noti.Notification_bm(iddd, title, message, url, "bt", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    }

                } else if (type.equals("h")) {

                    try {
                        bm = URLDecoder.decode(bm, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (ntype.equals("bt")) {
                        noti.Notification_custom(0, title, message, url, "bt", bm, sharedPreference
                                .getInt(this, "sund_chk1"), MainActivity.class);
                    } else if (ntype.equals("bi")) {
                        noti.Notification_custom(0, title, message, url, "bi", bm, sharedPreference
                                .getInt(this, "sund_chk1"), MainActivity.class);
                    }
                } else if (type.equals("st")) {

                    try {
                        bm = URLDecoder.decode(bm, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    myDB.execSQL("INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                            "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','s','" + bm + "','" + ntype + "','" + url + "');");
                    sharedPreference.putInt(this, "typee", 0);
                    Cursor c = myDB.rawQuery("select id from noti_cal", null);
                    c.moveToLast();
                    iddd = c.getInt(0);

                    myDB.close();
                    /*if (sharedPreference.getBoolean(this, "notiS_onoff") == true) {*/
                    if (ntype.equals("bt")) {
                        noti.Notification_bm(iddd, title, message, url, "bt", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    } else if (ntype.equals("bi")) {
                        noti.Notification_bm(iddd, title, message, url, "bi", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    } else {
                        noti.Notification_bm(iddd, title, message, url, "bt", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    }
                    /*}*/
                } else if (type.equals("w")) {

                    try {
                        bm = URLDecoder.decode(bm, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    myDB.execSQL("INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                            "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','w','" + bm + "','" + ntype + "','" + url + "');");
                    sharedPreference.putInt(this, "typee", 0);
                    Cursor c = myDB.rawQuery("select id from noti_cal", null);
                    c.moveToLast();
                    iddd = c.getInt(0);

                    myDB.close();

                    if (ntype.equals("bt")) {
                        noti.Notification_custom(iddd, title, message, url, "bt", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    } else if (ntype.equals("bi")) {
                        noti.Notification_custom(iddd, title, message, url, "bi", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    } else {
                        noti.Notification_bm(iddd, title, message, url, "bt", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    }

                } else if (type.equals("ns")) {
                    myDB.execSQL("INSERT INTO noti_cal(title,message,date,time,isclose,type,bm,ntype,url) values " +
                            "('" + title + "','" + message + "','" + date + "','" + time + "','" + isclose + "','ns','" + bm + "','" + ntype + "','" + url + "');");
                    Cursor c = myDB.rawQuery("select id from noti_cal", null);
                    c.moveToLast();
                    iddd = c.getInt(0);
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    myDB.close();
                } else if (type.equals("ins")) {
                    try {
                        bm = URLDecoder.decode(bm, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    /*if (sharedPreference.getBoolean(this, "notiS_onoff") == true) {*/
                    noti.Notification1(intent_id, title, message, url, "bi", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                    /*}*/
                } else if (type.equals("ap")) {
                    if (!appInstalledOrNot(pac)) {

                        if (ntype.equals("n")) {
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            noti.Notification1(intent_id, title, message, url, "bt", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                        } else if (ntype.equals("bt")) {
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            noti.Notification1(intent_id, title, message, url, "bt", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                        } else if (ntype.equals("bi")) {
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            noti.Notification1(intent_id, title, message, url, "bi", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                        } else if (ntype.equals("w")) {
                            try {
                                bm = URLDecoder.decode(bm, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            noti.Notification1(intent_id, title, message, url, "bi", bm, sharedPreference.getInt(this, "sund_chk1"), ST_Activity.class);
                        }
                    }
                } else if (type.equals("u")) {
                    sharedPreference.putInt(this, "gcmvcode", Integer.parseInt(message));
                    sharedPreference.putInt(this, "isvupdate", 1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            app_installed = false;
        }
        return app_installed;
    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        System.out.println("sendRegistrationToServer: " + token);
        SharedPreference sharedPreference = new SharedPreference();
        if (!sharedPreference.getString(this, "token").equals(token)) {
            sharedPreference.putString(this, "token", token);

            if (sharedPreference.getInt(FirebaseMessageService.this, "isvalid") == 0) {
                if (sharedPreference.getString(FirebaseMessageService.this, "token").length() > 0) {

                    ServerUtilities.gcmpost(token, Utils.android_id(FirebaseMessageService.this), Utils.versionname_get(FirebaseMessageService.this),
                            Utils.versioncode_get(FirebaseMessageService.this), FirebaseMessageService.this);
                    sharedPreference.putInt(FirebaseMessageService.this, "fcm_update", Utils.versioncode_get(FirebaseMessageService.this));

                }

            } else if (sharedPreference.getInt(FirebaseMessageService.this, "fcm_update") < Utils.versioncode_get(FirebaseMessageService.this)) {

                ServerUtilities.gcmupdate(FirebaseMessageService.this, Utils.versionname_get(FirebaseMessageService.this), Utils.versioncode_get(FirebaseMessageService.this), token);
                sharedPreference.putInt(FirebaseMessageService.this, "fcm_update", Utils.versioncode_get(FirebaseMessageService.this));
            }
        }

    }
}
