package com.example.astrovibe.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrovibe.data.models.Astrologer
import com.example.astrovibe.data.models.BirthLocation
import com.example.astrovibe.data.models.Resource
import com.example.astrovibe.data.models.User
import com.example.astrovibe.data.repo.AstrologerRepo
import com.example.astrovibe.data.repo.UserRepo
import com.example.astrovibe.data.utils.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepo,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Loading)
    val user: StateFlow<Resource<User>> = _user.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.O)
    fun createUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createUser(user).onStart {
                _user.value = Resource.Loading
            }.catch {
                _user.value = Resource.Error(it.message ?: "Something went wrong")
            }.collect { data ->
                _user.value = data
            }
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUser(userId).onStart {
                _user.value = Resource.Loading
            }.catch {
                _user.value = Resource.Error(it.message ?: "Something went wrong")
            }.collect { data ->
                _user.value = data
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserLocalData(): User {
        return User(
            fid = appPreferences.getUserId().toString(),
            name = appPreferences.getName().toString(),
            email = appPreferences.getEmail(),
            imageUrl = appPreferences.getImageUrl(),
            dateOfBirth = appPreferences.getDateOfBirth().toString(),
            timeOfBirth = appPreferences.getTimeOfBirth().toString(),
            contactNumber = appPreferences.getContactNumber().toString(),
            gender = appPreferences.getGender().toString(),
            birthLocation = appPreferences.getBirthLocation(),
            walletBalance = appPreferences.getWalletBalance(),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUserAppPreferences(user: User?) {
        if (user != null) {
            appPreferences.setUserId(user.fid)
            appPreferences.setName(user.name)
            appPreferences.setEmail(user.email)
            appPreferences.setImageUrl(user.imageUrl)
            appPreferences.setDateOfBirth(LocalDate.parse(user.dateOfBirth))
            appPreferences.setTimeOfBirth(LocalTime.parse(user.timeOfBirth))
            appPreferences.setContactNumber(user.contactNumber)
            appPreferences.setGender(user.gender)
            user.birthLocation?.let { appPreferences.setBirthLocation(it) }
            appPreferences.setWalletBalance(user.walletBalance)
        }
    }

    fun clearUserLocalData(){
        appPreferences.clearAll()
    }
}