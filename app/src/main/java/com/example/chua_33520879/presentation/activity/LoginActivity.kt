package com.example.chua_33520879.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.chua_33520879.presentation.composable.LogIn
import com.example.chua_33520879.presentation.viewmodel.AuthenticationViewModel
import com.example.chua_33520879.ui.theme.Chua_33520879Theme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.presentation.composable.Register
import com.example.chua_33520879.presentation.viewmodel.PatientViewModel
import com.example.chua_33520879.presentation.viewmodel.QuestionnaireViewModel
import kotlinx.coroutines.runBlocking

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: AuthenticationViewModel = ViewModelProvider(
            this, AuthenticationViewModel.AuthenticationViewModelFactory(this@LoginActivity)
        ) [AuthenticationViewModel::class.java]

        val questionaireVM: QuestionnaireViewModel = ViewModelProvider(
            this, QuestionnaireViewModel.QuestionnaireViewModelFactory(this@LoginActivity)
        ) [QuestionnaireViewModel::class.java]

        setContent {
            Chua_33520879Theme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Companion.Blue)
                ) { innerPadding ->
                    val navController: NavHostController = rememberNavController()
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Companion.Blue)
                    ) {
                        Column (
                            horizontalAlignment = Alignment.Companion.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier.padding(innerPadding)
                                .background(color = Color.Companion.Blue)
                                .padding(top = 75.dp)
                                .background(color = Color.Companion.White, shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                        ) {
                            Spacer(
                                modifier = Modifier.Companion.size(10.dp)
                            )
                            Box(
                                modifier = Modifier.Companion
                                    .background(
                                        Color.Companion.DarkGray,
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(100.dp)
                                    )
                                    .size(35.dp, 5.dp)
                            )
                            AuthenticationPageNavHost(navController, viewModel, questionaireVM)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AuthenticationPageNavHost(
        navController: NavHostController,
        viewModel: AuthenticationViewModel,
        questionaireVM: QuestionnaireViewModel
    ) {
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LogIn(
                    viewModel,
                    {
                        runBlocking {
                            val ret = questionaireVM.checkIfExist(AuthManager.getUserID()!!)
                            if (ret) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            } else {
                                startActivity(Intent(this@LoginActivity, QuestionnaireActivity::class.java))
                            }
                        }
                    },
                    navController
                )
            }
            composable("register") {
                Register(
                    viewModel,
                    navController
                )
            }
        }
    }
}