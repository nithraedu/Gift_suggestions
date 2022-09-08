package nithra.gift.suggestion.shop.birthday.marriage.Notification;

import android.content.Context;
import android.net.ParseException;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nithra.gift.suggestion.shop.birthday.marriage.Notification.HttpHandler;
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference;

public class ServerUtilities {
    private static JSONArray jArray;
    private static String result = null;

    static void gcmpost(String regId, String Emailid, String vername, int vercode, Context context) {
        SharedPreference sharedPreference = new SharedPreference();
        HttpHandler sh = new HttpHandler();
        String url = "https://nithra.mobi/appgcm/gcmi3/register.php";
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("email", Emailid);
            postDataParams.put("regId", regId);
            postDataParams.put("vname", vername);
            postDataParams.put("vcode", vercode + "");
            postDataParams.put("andver", Build.VERSION.RELEASE);
            postDataParams.put("sw", "");
            postDataParams.put("asw", sharedPreference.getString(context, "smallestWidth"));
            postDataParams.put("w", sharedPreference.getString(context, "widthPixels"));
            postDataParams.put("h", sharedPreference.getString(context, "heightPixels"));
            postDataParams.put("d", sharedPreference.getString(context, "density"));
            System.out.println("regId1 : " + regId);
            System.out.println("input : " + postDataParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = sh.makeServiceCall(url, postDataParams);
        System.out.println("response : " + result);
        try {
            System.out.println("ERROR----" + result);
            if (result == null) {
                System.out.println("ERROR----" + result + "1");
            } else {
                System.out.println("ERROR----" + result + "2");
                jArray = new JSONArray(result);
                JSONObject json_data = null;
                int isvalided;
                System.out.println("result---/" + result);
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    isvalided = json_data.getInt("isvalid");
                    sharedPreference.putInt(context, "isvalid", isvalided);
                    sharedPreference.putInt(context, "vcode", Utils.versioncode_get(context));
                    sharedPreference.putInt(context, "fcm_update", Utils.versioncode_get(context));
                }
            }
        } catch (JSONException e1) {
            System.out.println("ERROR----" + result + "3");
        } catch (ParseException e1) {
            System.out.println("ERROR----" + result + "4");
        }
    }

    static void gcmupdate(Context context, String vername, int vercode, String regid) {
        SharedPreference sharedPreference = new SharedPreference();
        HttpHandler sh = new HttpHandler();
        String url = "https://nithra.mobi/appgcm/gcmi3/register.php";
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("vname", vername);
            postDataParams.put("vcode", Integer.toString(vercode));
            postDataParams.put("email", "" + Utils.android_id(context));
            postDataParams.put("regid", regid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = sh.makeServiceCall(url, postDataParams);
        System.out.println("response : " + result);
        try {
            System.out.println("ERROR----" + result);
            if (result == null) {
                System.out.println("ERROR----" + result + "1");
            } else {
                System.out.println("ERROR----" + result + "2");
                jArray = new JSONArray(result);
                JSONObject json_data = null;
                System.out.println("result---/" + result);
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    sharedPreference.putInt(context, "fcm_update", Utils.versioncode_get(context));
                }
            }
        } catch (JSONException e1) {
            System.out.println("ERROR----" + result + "3");
        } catch (ParseException e1) {
            System.out.println("ERROR----" + result + "4");
        }
    }
}

