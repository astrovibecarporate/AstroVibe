package com.example.astrovibe.data.models

import java.time.LocalDate

data class ZodiacInfo(
    val name: String,
    val imageRes: Int,
    val description: String
)

data class ZodiacItem(
    val name: String,
    val imageRes: Int
)


data class UiState(
    val zodiacList: List<ZodiacItem> = emptyList(),
    val selectedZodiac: String? = null,
    val selectedDOB: LocalDate? = null,
    val zodiacDetails: ZodiacInfo? = null
)