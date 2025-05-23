package com.example.astrovibe.data.models

import java.time.LocalDate
import java.time.LocalTime

data class User(
    val id: Int,
    val name: String,
    val age: Int,
    val email: String,
    val password: String,
    val imageName: String?,
    val dateOfBirth: LocalDate,
    val timeOfBirth: LocalTime,
    val contactNumber: String,
    val place: String,
    val gender: String,
    val walletBalance: Double,
    val astrologerId: Int,
    val numerology : Int
)