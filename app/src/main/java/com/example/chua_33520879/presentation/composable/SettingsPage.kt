package com.example.chua_33520879.presentation.composable

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.icons.lucide.HeartPulse
import com.composables.icons.lucide.Lucide
import com.example.chua_33520879.R
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.data.patient.Patient
import com.example.chua_33520879.presentation.activity.LoginActivity
import com.example.chua_33520879.presentation.activity.QuestionnaireActivity
import com.example.chua_33520879.presentation.viewmodel.AuthenticationState
import com.example.chua_33520879.presentation.viewmodel.ClinicianViewModel
import com.example.chua_33520879.presentation.viewmodel.PatientViewModel
import com.example.chua_33520879.presentation.viewmodel.SettingsViewModel
import com.example.chua_33520879.presentation.viewmodel.UpdateState
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun SettingsPage(
    navController: NavController,
    viewModel: PatientViewModel,
    clinicVM: ClinicianViewModel,
    settingsVM: SettingsViewModel
) {
    val userID = AuthManager.getUserID()
    val userData = viewModel.getUserData(userID!!).collectAsStateWithLifecycle(Patient())
    val scrollState = rememberScrollState()

    val key = clinicVM.key.collectAsStateWithLifecycle()

    val updateState = settingsVM.state.collectAsStateWithLifecycle()
    val updateDialog = settingsVM.updateModal.collectAsStateWithLifecycle()
    val phoneNumber = settingsVM.phoneNumber.collectAsStateWithLifecycle()
    val username = settingsVM.name.collectAsStateWithLifecycle()

    val password = settingsVM.password.collectAsStateWithLifecycle()
    val passwordConfirm = settingsVM.passwordConfirm.collectAsStateWithLifecycle()
    val passwordDialog = settingsVM.passwordModal.collectAsStateWithLifecycle()

    val loginDialog = clinicVM.loginModal.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                "ACCOUNT",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 15.dp)
            )
            IconRow(Icons.Outlined.Person,userData.value.name)
            IconRow(Icons.Outlined.Phone, userData.value.phoneNumber)
            IconRow(Icons.Outlined.AccountBox, userData.value.userID.toString())

            Row (
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                UpdateProfileModel(
                    updateState.value,
                    { settingsVM.resetState() },
                    phoneNumber.value,
                    { settingsVM.setPhoneNumber(it) },
                    username.value,
                    { settingsVM.setName(it) },
                    { settingsVM.update(AuthManager.getUserID()!!) },
                    {
                        settingsVM.setName(userData.value.name)
                        settingsVM.setPhoneNumber(userData.value.phoneNumber)
                    },
                    { settingsVM.resetFields() },
                    updateDialog.value,
                    { settingsVM.setUpdateModal(it) }
                )

                ChangePasswordModel(
                    updateState.value,
                    { settingsVM.resetState() },
                    password.value,
                    { settingsVM.setPassword(it) },
                    passwordConfirm.value,
                    { settingsVM.setPasswordConfirm(it) },
                    { settingsVM.updatePassword(AuthManager.getUserID()!!) },
                    { settingsVM.resetFields() },
                    passwordDialog.value,
                    { settingsVM.setPasswordModel(it) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 15.dp), color = Color.Gray)

            Text(
                "OTHER SETTINGS",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 15.dp)
            )

            val context = LocalContext.current
            ClickableRow(Icons.Outlined.ExitToApp, "Log Out") {
                val sharedPref = context.getSharedPreferences(AuthManager.sharedPrefName, Context.MODE_PRIVATE).edit()
                AuthManager.logout(sharedPref)
                context.startActivity(Intent(context, LoginActivity::class.java))
                Toast.makeText(context, "Successfully Logged Out", Toast.LENGTH_LONG).show()
            }
            ClickableRow(Icons.Outlined.Person, "Clinician Login") {
                clinicVM.setLoginModal(true)
            }
        }
    }
    ClinicianLogin(
        loginDialog.value,
        key.value,
        { clinicVM.updateKey(it) },
        { clinicVM.validateLogin() },
        navController
    ) {
        clinicVM.setLoginModal(it)
    }
}

@Composable
fun IconRow(imageVector: ImageVector, value: String) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 25.dp)
                .size(30.dp)
        )
        Text(value)
    }
}

@Composable
fun ClickableRow(imageVector: ImageVector, value: String, onClick: () -> Unit) {
    Row (
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 25.dp)
                .size(30.dp)
        )
        Text(value, modifier = Modifier.weight(1f))
        Icon(
            Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 25.dp)
                .size(30.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileModel(
    state: UpdateState,
    resetState: () -> Unit,
    phoneNumber: String,
    setPhoneNumber: (String) -> Unit,
    username: String,
    setUsername: (String) -> Unit,
    update: () -> Unit,
    init: () -> Unit,
    resetField: () -> Unit,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
) {
    Button(
        onClick = { setShowDialog(true) },
        modifier = Modifier,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonColors(
            Color.Companion.Blue,
            Color.Companion.White,
            Color.Companion.Blue,
            Color.Companion.White
        ),
        contentPadding = PaddingValues(10.dp)
    ) {
        Text("Update Profile", fontSize = 17.sp)
    }

    if (showDialog) {
        LaunchedEffect(showDialog) {
            init()
        }
        BasicAlertDialog (
            onDismissRequest = {
                resetField()
                setShowDialog(false)
           },
        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .padding(vertical = 20.dp, horizontal = 15.dp)
            ) {
                Text(
                    "Profile Update",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                var context = LocalContext.current

                when (state) {
                    is UpdateState.Success -> {
                        resetState()
                        resetField()
                        Toast.makeText(context, "Your data is updated!", Toast.LENGTH_LONG).show()
                        setShowDialog(false)
                    }
                    is UpdateState.Error -> {
                        resetState()
                        val msg = state.msg
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                    else -> { }
                }

                OutlinedTextField(
                    value = username,
                    onValueChange = { setUsername(it) },
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

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { setPhoneNumber(it) },
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

                Row (
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Button(
                        onClick = update,
                        modifier = Modifier.Companion
                            .weight(1f)
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
                        Text("Update", fontSize = 17.sp)
                    }

                    Button(
                        onClick = { setShowDialog(false) },
                        modifier = Modifier.Companion
                            .weight(1f)
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
                        Text("Cancel", fontSize = 17.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicianLogin(
    showDialog: Boolean,
    key: String,
    updateKey: (String) -> Unit,
    validateLogin: () -> Boolean,
    navController: NavController,
    setDialog: (Boolean) -> Unit
) {
    if (showDialog) {
        BasicAlertDialog (
            onDismissRequest = { setDialog(false) },
        ) {
            Column (
                verticalArrangement = Arrangement.spacedBy(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .padding(vertical = 20.dp, horizontal = 15.dp)
            ) {
                Text(
                    "Clinician Login",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )

                PasswordField(
                    key,
                    "Clinician Key",
                    "Enter your clinician key"
                ) {
                    updateKey(it)
                }

                val context = LocalContext.current
                Button(
                    onClick = {
                        if (validateLogin()) {
                            updateKey("")
                            setDialog(false)
                            navController.navigate("Clinician")
                        } else {
                            Toast.makeText(context, "Incorrect Key", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
                    colors = ButtonColors(
                        Color.Companion.Blue,
                        Color.Companion.White,
                        Color.Companion.Blue,
                        Color.Companion.White
                    ),
                    contentPadding = PaddingValues(15.dp)
                ) {
                    Icon(Icons.Filled.ExitToApp, null, modifier = Modifier.padding(end = 15.dp))
                    Text("Clinician Login", fontSize = 17.sp)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordModel(
    state: UpdateState,
    resetState: () -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    passwordConfirm: String,
    setPasswordConfirm: (String) -> Unit,
    update: () -> Unit,
    resetField: () -> Unit,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit
) {
    Button(
        onClick = { setShowDialog(true) },
        modifier = Modifier,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonColors(
            Color.Companion.Blue,
            Color.Companion.White,
            Color.Companion.Blue,
            Color.Companion.White
        ),
        contentPadding = PaddingValues(10.dp)
    ) {
        Text("Change Password", fontSize = 17.sp)
    }

    if (showDialog) {
        BasicAlertDialog (
            onDismissRequest = {
                setShowDialog(false)
                resetField()
           },
        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .padding(vertical = 20.dp, horizontal = 15.dp)
            ) {
                Text(
                    "Change Password",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                var context = LocalContext.current

                when (state) {
                    is UpdateState.Success -> {
                        resetState()
                        resetField()
                        Toast.makeText(context, "Your password is changed!", Toast.LENGTH_LONG).show()
                        setShowDialog(false)
                    }
                    is UpdateState.Error -> {
                        resetState()
                        val msg = state.msg
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                    else -> { }
                }

                PasswordField (
                    password,
                    "Password",
                    "Enter your new password"
                ) {
                    setPassword(it)
                }

                PasswordField(
                    passwordConfirm,
                    "Confirm Password",
                    "Enter your new password again"
                ) {
                    setPasswordConfirm(it)
                }

                Row (
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Button(
                        onClick = update,
                        modifier = Modifier.Companion
                            .weight(1f)
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
                        Text("Update", fontSize = 17.sp)
                    }

                    Button(
                        onClick = {
                            setShowDialog(false)
                            resetField()
                        },
                        modifier = Modifier.Companion
                            .weight(1f)
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
                        Text("Cancel", fontSize = 17.sp)
                    }
                }
            }
        }
    }
}
