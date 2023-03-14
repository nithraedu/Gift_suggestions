package nithra.gift.suggestion.shop.birthday.marriage.support

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.text.format.DateUtils
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object Utils_Class {
    public var mProgress: ProgressDialog? = null
    @JvmStatic
    fun mProgress(context: Context?, txt: String?, aBoolean: Boolean?): ProgressDialog? {
        mProgress = ProgressDialog(context)
        mProgress!!.setMessage(txt)
        //mProgress.setCancelable(aBoolean);
        return mProgress
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun date_put(context: Context?, str: String?, `val`: Int) {
        val calendar = Calendar.getInstance()
        val next_hour = calendar.timeInMillis + `val` * DateUtils.DAY_IN_MILLIS
        val sdf1 = SimpleDateFormat("dd/M/yyyy")
        val results = Date(next_hour)
        val formatted = sdf1.format(results)
        val st2 = StringTokenizer(formatted, "/")
        val rep_day = st2.nextToken().toInt()
        var rep_month = st2.nextToken().toInt()
        val rep_year = st2.nextToken().toInt()
        rep_month = rep_month - 1
        val strdate = "$rep_day/$rep_month/$rep_year"
        val sharedPreference = SharedPreference()
        sharedPreference.putString(context!!, str!!, strdate)
    }

    fun versioncode_get(context: Context): Int {
        var pInfo: PackageInfo? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return pInfo!!.versionCode
    }

    fun versionname_get(context: Context): String {
        var pInfo: PackageInfo? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return pInfo!!.versionName
    }

    @JvmStatic
    fun toast_normal(context: Context?, str: String) {
        Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun toast_center(context: Context?, str: String) {
        val toast = Toast.makeText(context, "" + str, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun pad(str: String): String {
        var str = str
        if (str.length == 1) {
            str = "0$str"
        }
        return str
    }

    val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val Brand = Build.BRAND
            val Product = Build.PRODUCT
            return "$manufacturer-$model-$Brand-$Product"
        }

    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val connec = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connec.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @JvmStatic
    fun android_id(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}