package nithra.gift.suggestion.shop.birthday.marriage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class Utils_Class {public static ProgressDialog mProgress;

    public static ProgressDialog mProgress(Context context, String txt, Boolean aBoolean) {
        mProgress = new ProgressDialog(context);
        mProgress.setMessage(txt);
        //mProgress.setCancelable(aBoolean);
        return mProgress;
    }

    public static void date_put(Context context, String str, int val) {
        Calendar calendar = Calendar.getInstance();
        long next_hour = calendar.getTimeInMillis() + val * DateUtils.DAY_IN_MILLIS;

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");
        Date results = new Date(next_hour);
        String formatted = sdf1.format(results);

        StringTokenizer st2 = new StringTokenizer(formatted, "/");
        int rep_day = Integer.parseInt(st2.nextToken());
        int rep_month = Integer.parseInt(st2.nextToken());
        int rep_year = Integer.parseInt(st2.nextToken());

        rep_month = rep_month - 1;

        String strdate = rep_day + "/" + rep_month + "/" + rep_year;

        SharedPreference sharedPreference = new SharedPreference();
        sharedPreference.putString(context, str, strdate);
    }

    public static int versioncode_get(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pInfo.versionCode;
    }

    public static String versionname_get(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pInfo.versionName;
    }

    public static void toast_normal(Context context, String str) {
        Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show();
    }

    public static void toast_center(Context context, String str) {
        Toast toast = Toast.makeText(context, "" + str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String pad(String str) {
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String Brand = Build.BRAND;
        String Product = Build.PRODUCT;
        return manufacturer + "-" + model + "-" + Brand + "-" + Product;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connec.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String android_id(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }
}



