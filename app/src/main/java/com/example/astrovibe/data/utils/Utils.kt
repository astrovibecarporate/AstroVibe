package com.example.astrovibe.data.utils

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.example.astrovibe.R
import com.example.astrovibe.data.models.ZodiacItem
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.time.LocalDate

object Utils {

    const val BASE_URL = "http://192.168.1.2:9999"
    const val API_KEY = "AIzaSyCXTsI9xZM3crB6gJUhiNWBRx2LtvhtoR8"
    val LightSaffron = Color(0xFFFFF4E1) // pale saffron
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

    suspend fun compressImage(context: android.content.Context, uri: Uri): Bitmap? = withContext(
        Dispatchers.IO) {
        try {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val compressedBytes = stream.toByteArray()
            android.graphics.BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun uploadImage(
        userPhoneNumber: String,
        bitmap: Bitmap,
        onProgress: (Float) -> Unit,
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit
    ) {
        val storageRef = Firebase.storage.reference.child("astrologers/photos/$userPhoneNumber.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()

        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
            onProgress((progress / 100).toFloat())
        }.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }.addOnFailureListener {
                onFailure()
            }
        }.addOnFailureListener {
            onFailure()
        }
    }

}