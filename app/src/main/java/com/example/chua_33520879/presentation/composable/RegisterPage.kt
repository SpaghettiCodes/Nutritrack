package com.example.chua_33520879.presentation.composable

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.chua_33520879.presentation.viewmodel.AuthenticationState
import com.example.chua_33520879.presentation.viewmodel.AuthenticationViewModel

@Composable
fun Register(
    viewModel: AuthenticationViewModel,
    navController: NavHostController
) {
    var userID = viewModel.userID.collectAsStateWithLifecycle()
    var phoneNumber = viewModel.phoneNumber.collectAsStateWithLifecycle()
    var name = viewModel.name.collectAsStateWithLifecycle()
    var password = viewModel.password.collectAsStateWithLifecycle()
    var passwordConfirm = viewModel.passwordConfirm.collectAsStateWithLifecycle()

    var IDList = viewModel.IDList.collectAsStateWithLifecycle(emptyList<Int>())
    var state = viewModel.state.collectAsStateWithLifecycle()
    var context = LocalContext.current

    var scrollState = rememberScrollState()

    when (state.value) {
        is AuthenticationState.Success -> {
            viewModel.resetState()
            Toast.makeText(context, "Register success! You may log in now", Toast.LENGTH_LONG).show()
            navController.navigate("login")
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
        modifier = Modifier
            .fillMaxHeight()
            .padding(15.dp)
            .verticalScroll(scrollState)
            .heightIn(min = 450.dp)
    ) {
        val scrollState = rememberScrollState()
        Text(
            "Register",
            fontWeight = FontWeight.Companion.Bold,
            fontSize = 20.sp,
            modifier = Modifier.Companion.padding(bottom = 25.dp)
        )
        Column (
            modifier = Modifier.verticalScroll(scrollState).weight(1f, fill = false)
        ) {
            DropdownMenu(IDList.value.map { it.toString() }, userID, { viewModel.setUserID(it) })
            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = { viewModel.setPhoneNumber(it) },
                label = {
                    Text(
                        "Phone Number",
                        fontWeight = FontWeight.Companion.Bold,
                        fontSize = 15.sp
                    )
                },
                placeholder = {
                    Text("Enter your number")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Companion.Gray,
                    unfocusedBorderColor = Color.Companion.Gray,
                    focusedContainerColor = Color.Companion.Transparent,
                    unfocusedContainerColor = Color.Companion.Transparent
                ),
                singleLine = true,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp)
            )
            OutlinedTextField(
                value = name.value,
                onValueChange = { viewModel.setName(it) },
                label = {
                    Text(
                        "Name",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                placeholder = {
                    Text("Enter your name")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(15.dp)
            )
            PasswordField(
                password.value,
                "Password",
                "Enter your password"
            ) {
                viewModel.setPassword(it)
            }
            PasswordField(
                passwordConfirm.value,
                "Confirm Password",
                "Enter your password again"
            ) {
                viewModel.setPasswordConfirm(it)
            }
            Text(
                "This app is only for pre-registered users. Please enter your ID, phone number and password to claim your account.",
                fontSize = 14.sp,
                modifier = Modifier.Companion.padding(vertical = 25.dp)
            )
        }

        Button(
            onClick = {
                viewModel.register()
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
            Text("Register", fontSize = 17.sp)
        }
        Button(
            onClick = {
                navController.navigate("login")
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
            Text("Login", fontSize = 17.sp)
        }
    }
}
