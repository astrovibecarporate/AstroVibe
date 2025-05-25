package com.example.astrovibe.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.astrovibe.R
import com.example.astrovibe.data.models.UiState
import com.example.astrovibe.data.models.ZodiacInfo
import com.example.astrovibe.data.repo.AstrologerRepo
import com.example.astrovibe.data.utils.Utils
import com.example.astrovibe.data.utils.Utils.allZodiacs
import com.example.astrovibe.data.utils.Utils.getZodiacFromDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class HoroscopeViewModel @Inject constructor(
    private val repository: AstrologerRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        _uiState.update { it.copy(zodiacList = Utils.allZodiacs) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDOBSelected(date: LocalDate) {
        val zodiac = getZodiacFromDate(date)
        _uiState.update {
            it.copy(
                selectedDOB = date,
                selectedZodiac = zodiac,
                zodiacDetails = mockZodiacDetails(zodiac)
            )
        }
    }

    fun onZodiacSelected(zodiac: String) {
        _uiState.update {
            it.copy(
                selectedZodiac = zodiac,
                zodiacDetails = mockZodiacDetails(zodiac)
            )
        }
    }

    private fun mockZodiacDetails(zodiac: String): ZodiacInfo {
        val imageRes = allZodiacs.find { it.name == zodiac }?.imageRes ?: R.drawable.aries
        return ZodiacInfo(
            name = zodiac,
            imageRes = imageRes,
            description = "Your zodiac sign is $zodiac. This is a placeholder description."
        )
    }
}