package com.example.astrovibe.data.utils


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.astrovibe.data.models.BirthLocation
import java.time.LocalDate
import java.time.LocalTime

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("astro_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_IS_ASTROLOGER = "is_astrologer"
        private const val KEY_PHONE_NUMBER = "phone_number"

        // User fields
        private const val KEY_USER_ID = "user_id"
        private const val KEY_NAME = "user_name"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_IMAGE_URL = "user_image_url"
        private const val KEY_DOB = "user_dob"
        private const val KEY_TOB = "user_tob"
        private const val KEY_CONTACT = "user_contact"
        private const val KEY_GENDER = "user_gender"
        private const val KEY_WALLET = "user_wallet"

        // BirthLocation fields
        private const val KEY_CITY = "user_city"
        private const val KEY_STATE = "user_state"
        private const val KEY_COUNTRY = "user_country"
        private const val KEY_LATITUDE = "user_latitude"
        private const val KEY_LONGITUDE = "user_longitude"
    }

    // Login / Auth
    fun setLoggedIn(value: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun setPhoneNumber(phone: String) {
        prefs.edit().putString(KEY_PHONE_NUMBER, phone).apply()
    }

    fun getPhoneNumber(): String? = prefs.getString(KEY_PHONE_NUMBER, null)

    fun setUserIfUserAstrologer(isAstrologer: Boolean) {
        prefs.edit().putBoolean(KEY_IS_ASTROLOGER, isAstrologer).apply()
    }

    fun isUserAstrologer(): Boolean = prefs.getBoolean(KEY_IS_ASTROLOGER, false)

    // User fields
    fun setUserId(fid: String) = prefs.edit().putString(KEY_USER_ID, fid).apply()
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun setName(name: String) = prefs.edit().putString(KEY_NAME, name).apply()
    fun getName(): String? = prefs.getString(KEY_NAME, null)

    fun setEmail(email: String?) = prefs.edit().putString(KEY_EMAIL, email).apply()
    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    fun setImageUrl(url: String?) = prefs.edit().putString(KEY_IMAGE_URL, url).apply()
    fun getImageUrl(): String? = prefs.getString(KEY_IMAGE_URL, null)

    fun setDateOfBirth(dob: LocalDate) {
        prefs.edit().putString(KEY_DOB, dob.toString()).apply() // ISO-8601 format: yyyy-MM-dd
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateOfBirth(): LocalDate? {
        val dobString = prefs.getString(KEY_DOB, null)
        return dobString?.let { LocalDate.parse(it) }
    }

    fun setTimeOfBirth(tob: LocalTime) {
        prefs.edit().putString(KEY_TOB, tob.toString()).apply() // Format: HH:mm:ss.nnn
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeOfBirth(): LocalTime? {
        val tobString = prefs.getString(KEY_TOB, null)
        return tobString?.let { LocalTime.parse(it) }
    }

    fun setContactNumber(contact: String) = prefs.edit().putString(KEY_CONTACT, contact).apply()
    fun getContactNumber(): String? = prefs.getString(KEY_CONTACT, null)

    fun setGender(gender: String) = prefs.edit().putString(KEY_GENDER, gender).apply()
    fun getGender(): String? = prefs.getString(KEY_GENDER, null)

    fun setWalletBalance(wallet: Double) = prefs.edit().putFloat(KEY_WALLET, wallet.toFloat()).apply()
    fun getWalletBalance(): Double = prefs.getFloat(KEY_WALLET, 0.0f).toDouble()

    // Birth Location
    fun setBirthLocation(location: BirthLocation) {
        prefs.edit().apply {
            putString(KEY_CITY, location.city)
            putString(KEY_STATE, location.state)
            putString(KEY_COUNTRY, location.country)
            putFloat(KEY_LATITUDE, location.latitude.toFloat())
            putFloat(KEY_LONGITUDE, location.longitude.toFloat())
            apply()
        }
    }

    fun getBirthLocation(): BirthLocation {
        return BirthLocation(
            city = prefs.getString(KEY_CITY, "") ?: "",
            state = prefs.getString(KEY_STATE, "") ?: "",
            country = prefs.getString(KEY_COUNTRY, "") ?: "",
            latitude = prefs.getFloat(KEY_LATITUDE, 0.0f).toDouble(),
            longitude = prefs.getFloat(KEY_LONGITUDE, 0.0f).toDouble()
        )
    }

    fun clearAll() = prefs.edit().clear().apply()
}
