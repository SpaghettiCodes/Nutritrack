package com.example.chua_33520879.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.data.patient.Patient
import com.example.chua_33520879.data.patient.PatientRepository
import com.example.chua_33520879.data.patient.ValidatePhoneNumber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthenticationViewModel(context: Context): ViewModel() {
    private val patientRepo: PatientRepository = PatientRepository(context)

    val _state = MutableStateFlow<AuthenticationState>(AuthenticationState.Initial)
    val state = _state.asStateFlow()
    fun resetState() {
        _state.value = AuthenticationState.Initial
    }

    val _userID = MutableStateFlow("")
    val userID = _userID.asStateFlow()
    fun setUserID(value: String) {
        _userID.value = value
    }

    val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()
    fun setPhoneNumber(value: String) {
        _phoneNumber.value = value
    }

    val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    fun setPassword(value: String) {
        _password.value = value
    }

    val _passwordConfirm = MutableStateFlow("")
    val passwordConfirm = _passwordConfirm.asStateFlow()
    fun setPasswordConfirm(value: String) {
        _passwordConfirm.value = value
    }

    val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    fun setName(value: String) {
        _name.value = value
    }

    fun logIn(sharedPreferences: SharedPreferences.Editor) =
        viewModelScope.launch {
            _state.value = AuthenticationState.Initial
            if (_userID.value.isEmpty()) {
                _state.value = AuthenticationState.Error("Must select an existing ID")
                return@launch
            }

            val userID = _userID.value.toInt()

            if (patientRepo.isRegistered(userID)) {
                if (patientRepo.checkPassword(userID, password.value)) {
                    _state.value = AuthenticationState.Success
                    AuthManager.login(userID, sharedPreferences)
                } else {
                    _state.value = AuthenticationState.Error("Incorrect Credentials")
                }
            } else {
                _state.value = AuthenticationState.Error("User not registered")
            }
        }

     fun register() =
         viewModelScope.launch {
             if (_userID.value.isEmpty()) {
                 _state.value = AuthenticationState.Error("Must select an existing ID")
                 return@launch
             }

             if (patientRepo.isRegistered(_userID.value.toInt())) {
                 _state.value = AuthenticationState.Error("User already registered")
                 return@launch
             }

             val attemptUserNumber = patientRepo.getPhoneNumber(userID.value.toInt())

             if (_phoneNumber.value.isEmpty()) {
                 _state.value = AuthenticationState.Error("Phone number cannot be empty!")
                 return@launch
             }

             if (!ValidatePhoneNumber(_phoneNumber.value)) {
                 _state.value = AuthenticationState.Error("Invalid phone number")
                 return@launch
             }

             if (attemptUserNumber != _phoneNumber.value) {
                 _state.value = AuthenticationState.Error("Phone number do not match")
                 return@launch
             }

             if (_name.value.isEmpty()) {
                 _state.value = AuthenticationState.Error("Name cannot be empty!")
                 return@launch
             }

             if (_password.value.isEmpty()) {
                 _state.value = AuthenticationState.Error("Password cannot be empty!")
                 return@launch
             }

             if (_password.value != _passwordConfirm.value) {
                 _state.value = AuthenticationState.Error("Password and password confirmation do not match!")
                 return@launch
             }

             patientRepo.setAsRegistered(_userID.value.toInt(), _password.value, _name.value)
             _state.value = AuthenticationState.Success
         }


    val IDList: Flow<List<Int>> = patientRepo.getIDList()

    class AuthenticationViewModelFactory(context: Context): ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AuthenticationViewModel(context) as T
    }
}

sealed interface AuthenticationState{
    object Initial: AuthenticationState
    object Success: AuthenticationState
    data class Error(val msg: String): AuthenticationState
}