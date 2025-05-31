package com.example.astrovibe.data.models

import java.time.LocalDate
import java.time.LocalTime

data class User(
    val fid: String,
    val name: String,
    val email: String?,
    val imageUrl: String? = null,
    val dateOfBirth: String? = null,
    val timeOfBirth: String? = null,
    val contactNumber: String,
    val gender: String,
    val birthLocation: BirthLocation?,
    val walletBalance: Double,
)

