package nithra.gift.suggestion.shop.birthday.marriage

import android.content.Context
import android.content.SharedPreferences

class SharedPreference {
    var prefrence: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    fun putString(context: Context, text: String, text1: String) {
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = prefrence!!.edit()
        editor!!.putString(text, text1)
        editor!!.commit()
    }

    fun getString(context: Context, PREFS_KEY: String): String? {
        val text: String?
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = prefrence!!.getString(PREFS_KEY, "")
        return text
    }

    fun removeString(context: Context, PREFS_KEY: String) {
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = prefrence!!.edit()
        editor!!.remove(PREFS_KEY)
        editor!!.commit()
    }

    fun putInt(context: Context, text: String, text1: Int) {
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = prefrence!!.edit()
        editor!!.putInt(text, text1)
        editor!!.commit()
    }

    fun getInt(context: Context, PREFS_KEY: String): Int {
        val text: Int
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = prefrence!!.getInt(PREFS_KEY, 0)
        return text
    }

    fun removeInt(context: Context, PREFS_KEY: String) {
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = prefrence!!.edit()
        editor!!.remove(PREFS_KEY)
        editor!!.commit()
    }

    fun putBoolean(context: Context, text: String, text1: Boolean) {
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = prefrence!!.edit()
        editor!!.putBoolean(text, text1!!)
        editor!!.commit()
    }

    fun getBoolean(context: Context, PREFS_KEY: String): Boolean {
        val text: Boolean
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        text = prefrence!!.getBoolean(PREFS_KEY, true)
        return text
    }

    fun removeBoolean(context: Context, PREFS_KEY: String) {
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = prefrence!!.edit()
        editor!!.remove(PREFS_KEY)
        editor!!.commit()
    }

    fun clearSharedPreference(context: Context) {
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        editor = prefrence!!.edit()
        editor!!.clear()
        editor!!.commit()
    }

    companion object {
        const val PREFS_NAME = ""
    }
}