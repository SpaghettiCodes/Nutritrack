package com.example.chua_33520879.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chua_33520879.data.patient.PatientRepository
import com.example.chua_33520879.data.patient.ValidatePhoneNumber
import com.example.chua_33520879.data.patient.hashPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(context: Context): ViewModel() {
    private val patientRepo: PatientRepository = PatientRepository(context)

    val _updateModal = MutableStateFlow(false)
    val updateModal = _updateModal.asStateFlow()
    fun setUpdateModal(value: Boolean) {
        _updateModal.value = value
    }

    val _passwordModal = MutableStateFlow(false)
    val passwordModal = _passwordModal.asStateFlow()
    fun setPasswordModel(value: Boolean) {
        _passwordModal.value = value
    }

    val _state = MutableStateFlow<UpdateState>(UpdateState.Initial)
    val state = _state.asStateFlow()
    fun resetState() {
        _state.value = UpdateState.Initial
    }

    val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()
    fun setPhoneNumber(value: String) {
        _phoneNumber.value = value
    }

    val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    fun setName(value: String) {
        _name.value = value
    }

    val _password = MutableStateFlow("")
    val _passwordConfirm = MutableStateFlow("")
    val password = _password.asStateFlow()
    val passwordConfirm = _passwordConfirm.asStateFlow()

    fun setPassword(value: String) {
        _password.value = value
    }

    fun setPasswordConfirm(value: String) {
        _passwordConfirm.value = value
    }

    fun update(_userID: Int) =
        viewModelScope.launch {
            if (_name.value.isEmpty()) {
                _state.value = UpdateState.Error("Name cannot be empty")
                return@launch
            }

            if (_phoneNumber.value.isEmpty()) {
                _state.value = UpdateState.Error("Phone number cannot be empty")
                return@launch
            }

            if (!ValidatePhoneNumber(_phoneNumber.value)) {
                _state.value = UpdateState.Error("Invalid phone number")
                return@launch
            }

            val userData = patientRepo.getDetails(_userID).first()
            patientRepo.update(userData.copy(
                name = _name.value,
                phoneNumber = _phoneNumber.value
            ))
            _state.value = UpdateState.Success
        }

    fun updatePassword(_userID: Int) =
        viewModelScope.launch {
            if (_password.value.isEmpty()) {
                _state.value = UpdateState.Error("Password cannot be empty")
                return@launch
            }

            if (_password.value != _passwordConfirm.value) {
                _state.value = UpdateState.Error("Password do not match")
                return@launch
            }

            val userData = patientRepo.getDetails(_userID).first()
            patientRepo.update(userData.copy(
                password = hashPassword(_password.value)
            ))
            _state.value = UpdateState.Success
            resetFields()
        }

    fun resetFields() {
        _password.value = ""
        _passwordConfirm.value = ""
        _name.value = ""
        _phoneNumber.value = ""
    }

    class factory(context: Context): ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(context) as T
    }
}

sealed interface UpdateState{
    object Initial: UpdateState
    object Success: UpdateState
    data class Error(val msg: String): UpdateState
}