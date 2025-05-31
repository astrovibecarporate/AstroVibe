package com.example.astrovibe.data.models

data class BirthLocation(
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    override fun toString(): String {
        return listOfNotNull(city, state, country).filter { it.isNotBlank() }.joinToString(", ")
    }
}