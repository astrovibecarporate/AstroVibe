package com.example.astrovibe.data.models


import java.time.LocalDate
import java.time.LocalTime

data class UserRegistrationData(
    val fid: String,
    val name: String,
    val gender: String,
    val email: String,
    val phone: String,
    val dob: LocalDate?,
    val tob: LocalTime?,
    val placeOfBirth: BirthLocation
)



