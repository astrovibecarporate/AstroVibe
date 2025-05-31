package com.example.astrovibe.data.utils


import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("astro_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_IS_ASTROLOGER = "is_astrologer"
        private const val KEY_PHONE_NUMBER = "phone_number"
    }

    fun setLoggedIn(value: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setPhoneNumber(phone: String) {
        prefs.edit().putString(KEY_PHONE_NUMBER, phone).apply()
    }

    fun getPhoneNumber(): String? {
        return prefs.getString(KEY_PHONE_NUMBER, null)
    }

    fun setUserIfUserAstrologer(isAstrologer: Boolean){
        prefs.edit().putBoolean(KEY_IS_ASTROLOGER, isAstrologer).apply()
    }

    fun isUserAstrologer(): Boolean {
        return prefs.getBoolean(KEY_IS_ASTROLOGER, false)

    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
