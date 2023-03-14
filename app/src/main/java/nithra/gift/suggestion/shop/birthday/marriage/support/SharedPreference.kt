package nithra.gift.suggestion.shop.birthday.marriage.support

import android.content.Context
import android.content.SharedPreferences

class SharedPreference {
    private var prefrence: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
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
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefrence!!.getString(PREFS_KEY, "")
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
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefrence!!.getInt(PREFS_KEY, 0)
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
        prefrence = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return prefrence!!.getBoolean(PREFS_KEY, true)
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