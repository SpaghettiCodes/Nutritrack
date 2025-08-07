package com.example.chua_33520879.presentation.composable

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.Lucide
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.presentation.viewmodel.AuthenticationState
import com.example.chua_33520879.presentation.viewmodel.AuthenticationViewModel

@Composable
fun LogIn(
    viewModel: AuthenticationViewModel,
    onSuccess: () -> Unit,
    navController: NavHostController
) {
    var userID = viewModel.userID.collectAsStateWithLifecycle()
    var password = viewModel.password.collectAsStateWithLifecycle()
    var IDList = viewModel.IDList.collectAsStateWithLifecycle(emptyList<Int>())
    var state = viewModel.state.collectAsStateWithLifecycle()
    var context = LocalContext.current
    var scrollState = rememberScrollState()

    when (state.value) {
        is AuthenticationState.Success -> {
            viewModel.resetState()
            onSuccess()
        }
        is AuthenticationState.Error -> {
            viewModel.resetState()
            val msg = (state.value as AuthenticationState.Error).msg
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
        else -> { }
    }

    Column(
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(15.dp).verticalScroll(scrollState)
    ) {
        Text(
            "Log in",
            fontWeight = FontWeight.Companion.Bold,
            fontSize = 20.sp,
            modifier = Modifier.Companion.padding(bottom = 25.dp)
        )
        DropdownMenu(IDList.value.map { it.toString() }, userID, { viewModel.setUserID(it) })
        PasswordField(password.value, "Password", "Enter your password") { viewModel.setPassword(it) }
        Text(
            "This app is only for pre-registered users. Please enter your ID, phone number and password to claim your account.",
            fontSize = 14.sp,
            modifier = Modifier.Companion.padding(vertical = 25.dp)
        )

        val sharedPref = LocalContext.current.getSharedPreferences(
            AuthManager.sharedPrefName,
            Context.MODE_PRIVATE
        ).edit()

        Button(
            onClick = {
                viewModel.logIn(sharedPref)
            },
            modifier = Modifier.Companion.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
            colors = ButtonColors(
                Color.Companion.Blue,
                Color.Companion.White,
                Color.Companion.Blue,
                Color.Companion.White
            ),
            contentPadding = PaddingValues(15.dp)
        ) {
            Text("Continue", fontSize = 17.sp)
        }
        Button(
            onClick = {
                navController.navigate("register")
            },
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(top = 15.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
            colors = ButtonColors(
                Color.Companion.Blue,
                Color.Companion.White,
                Color.Companion.Blue,
                Color.Companion.White
            ),
            contentPadding = PaddingValues(15.dp)
        ) {
            Text("Register", fontSize = 17.sp)
        }
    }
}
