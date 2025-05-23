package com.example.astrovibe.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrovibe.data.models.Astrologer
import com.example.astrovibe.data.models.Resource
import com.example.astrovibe.data.models.User
import com.example.astrovibe.data.repo.AstrologerRepo
import com.example.astrovibe.data.repo.AstrologerRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AstrologerViewModel @Inject constructor(
    private val repository: AstrologerRepo
) : ViewModel() {

    private val _astrologers = MutableStateFlow<Resource<List<Astrologer>>>(Resource.Loading)
    val astrologers: StateFlow<Resource<List<Astrologer>>> = _astrologers.asStateFlow()

    init {
        fetchAstrologersList()
    }

    private fun fetchAstrologersList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchAstrologers().onStart {
                _astrologers.value = Resource.Loading
            }.catch {
                _astrologers.value = Resource.Error(it.message ?: "Something went wrong")
            }.collect { data ->
                _astrologers.value = data
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserData(): User{
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