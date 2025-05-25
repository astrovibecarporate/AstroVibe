package com.example.astrovibe.data.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.astrovibe.R
import com.example.astrovibe.data.models.ZodiacItem
import java.time.LocalDate

object Utils {

    const val BASE_URL = "http://192.168.1.8:9999"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getZodiacFromDate(date: LocalDate): String {
        val (day, month) = date.dayOfMonth to date.monthValue
        return when {
            month == 3 && day >= 21 || month == 4 && day <= 19 -> "Aries"
            month == 4 && day >= 20 || month == 5 && day <= 20 -> "Taurus"
            month == 5 && day >= 21 || month == 6 && day <= 20 -> "Gemini"
            month == 6 && day >= 21 || month == 7 && day <= 22 -> "Cancer"
            month == 7 && day >= 23 || month == 8 && day <= 22 -> "Leo"
            month == 8 && day >= 23 || month == 9 && day <= 22 -> "Virgo"
            month == 9 && day >= 23 || month == 10 && day <= 22 -> "Libra"
            month == 10 && day >= 23 || month == 11 && day <= 21 -> "Scorpio"
            month == 11 && day >= 22 || month == 12 && day <= 21 -> "Sagittarius"
            month == 12 && day >= 22 || month == 1 && day <= 19 -> "Capricorn"
            month == 1 && day >= 20 || month == 2 && day <= 18 -> "Aquarius"
            else -> "Pisces"
        }
    }

    val allZodiacs = listOf(
        ZodiacItem("Aries", R.drawable.aries),
        ZodiacItem("Taurus", R.drawable.taurus),
        ZodiacItem("Gemini", R.drawable.gemini),
        ZodiacItem("Cancer", R.drawable.cancer),
        ZodiacItem("Leo", R.drawable.leo),
        ZodiacItem("Virgo", R.drawable.virgo),
        ZodiacItem("Libra", R.drawable.libra),
        ZodiacItem("Scorpio", R.drawable.scorpio),
        ZodiacItem("Sagittarius", R.drawable.sagittarius),
        ZodiacItem("Capricorn", R.drawable.capricorn),
        ZodiacItem("Aquarius", R.drawable.aquarius),
        ZodiacItem("Pisces", R.drawable.pisces)
    )

}