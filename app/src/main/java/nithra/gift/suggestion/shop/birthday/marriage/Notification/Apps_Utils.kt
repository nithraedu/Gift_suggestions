package nithra.gift.suggestion.shop.birthday.marriage.Notification

import android.content.Context
import android.text.format.DateUtils
import nithra.gift.suggestion.shop.birthday.marriage.SharedPreference
import java.text.SimpleDateFormat
import java.util.*

object Apps_Utils {
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
}