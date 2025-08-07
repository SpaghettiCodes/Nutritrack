package com.example.chua_33520879.data

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object AuthManager {
    var _userID: Int? = null
    val sharedPrefName = "nutricoach"

    fun initialize(sharedPref: SharedPreferences) {
        if (sharedPref.contains("log_in")) {
            _userID = sharedPref.getInt("log_in", 0) // should never assign default value
        }
    }

    fun getUserID(): Int? {
        return _userID
    }

    fun login(userID: Int, sharedPref: SharedPreferences.Editor) {
        _userID = userID
        sharedPref.putInt("log_in", userID)
        sharedPref.apply()
    }

    fun logout(sharedPref: SharedPreferences.Editor) {
        _userID = null
        sharedPref.remove("log_in")
        sharedPref.commit()
    }

    fun isLoggedIn(): Boolean {
        return _userID != null
    }
}