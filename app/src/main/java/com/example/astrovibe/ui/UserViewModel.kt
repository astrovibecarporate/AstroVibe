package com.example.astrovibe.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.astrovibe.data.models.User
import com.example.astrovibe.data.repo.AstrologerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: AstrologerRepo
) : ViewModel() {


    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserData(): User {
        val user = User(
            id = 1,
            name = "John Doe",
            age = 28,
            email = "john.doe@gmail.com",
            password = "Secret@123",
            imageName = null,
            dateOfBirth = LocalDate.of(1995, 12, 15),
            timeOfBirth = LocalTime.of(10, 30, 0),
            contactNumber = "9876543210",
            place = "New York",
            gender = "Male",
            walletBalance = 1500.75,
            astrologerId = 101,
            numerology = 1
        )
        return user
    }

}