package com.example.chua_33520879.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.Lucide
import com.example.chua_33520879.R
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.presentation.composable.ClinicianPage
import com.example.chua_33520879.presentation.composable.FoodAnalysisPage
import com.example.chua_33520879.presentation.composable.HomePage
import com.example.chua_33520879.presentation.composable.InsightPage
import com.example.chua_33520879.presentation.composable.NutriCoachPage
import com.example.chua_33520879.presentation.composable.SettingsPage
import com.example.chua_33520879.presentation.viewmodel.CalorieTrackerViewModel
import com.example.chua_33520879.presentation.viewmodel.ClinicianViewModel
import com.example.chua_33520879.presentation.viewmodel.NutricoachViewModel
import com.example.chua_33520879.presentation.viewmodel.PatientViewModel
import com.example.chua_33520879.presentation.viewmodel.SettingsViewModel
import com.example.chua_33520879.ui.theme.Chua_33520879Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val patientViewModel: PatientViewModel = ViewModelProvider(
            this, PatientViewModel.PatientViewModelFactory(this@MainActivity)
        ) [PatientViewModel::class.java]
        patientViewModel.isFruitScoreOptimal(AuthManager.getUserID()!!)

        val nutricoachViewModel: NutricoachViewModel = ViewModelProvider(
            this, NutricoachViewModel.factory(this@MainActivity)
        ) [NutricoachViewModel::class.java]

        val clinicianViewModel: ClinicianViewModel = ViewModelProvider(
            this, ClinicianViewModel.factory(this@MainActivity)
        ) [ClinicianViewModel::class.java]

        val settingsViewModel: SettingsViewModel = ViewModelProvider(
            this, SettingsViewModel.factory(this@MainActivity)
        ) [SettingsViewModel::class.java]

        val calorieTrackerViewModel: CalorieTrackerViewModel = ViewModelProvider(
            this, CalorieTrackerViewModel.factory(this@MainActivity)
        ) [CalorieTrackerViewModel::class.java]

        setContent {
            Chua_33520879Theme {
                val navController: NavHostController = rememberNavController()
                var curNav = remember { mutableStateOf("") }
                Scaffold(
                    modifier = Modifier.Companion.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(navController, curNav)
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.Companion.padding(innerPadding)
                    ) {
                        MainPageNavHost(
                            navController,
                            curNav,
                            patientViewModel,
                            nutricoachViewModel,
                            clinicianViewModel,
                            settingsViewModel,
                            calorieTrackerViewModel
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun MainPageNavHost(
        navController: NavHostController,
        url: MutableState<String>,
        patientViewModel: PatientViewModel,
        nutricoachViewModel: NutricoachViewModel,
        clinicianViewModel: ClinicianViewModel,
        settingsViewModel: SettingsViewModel,
        calorieTrackerViewModel: CalorieTrackerViewModel
    ) {
        NavHost(
            navController = navController,
            startDestination = "Home"
        ) {
            composable("Home") {
                url.value = "Home"
                HomePage(navController, patientViewModel)
            }
            composable("Insights") {
                url.value = "Insights"
                InsightPage(navController, patientViewModel)
            }
            composable("NutriCoach") {
                url.value = "NutriCoach"
                NutriCoachPage(navController, patientViewModel, nutricoachViewModel)
            }
            composable("Food Analysis") {
                url.value = "NutriCoach"
                FoodAnalysisPage(navController, calorieTrackerViewModel)
            }
            composable("Settings") {
                url.value = "Settings"
                SettingsPage(navController, patientViewModel, clinicianViewModel, settingsViewModel)
            }
            composable("Clinician") {
                url.value = "Settings"
                ClinicianPage(navController, clinicianViewModel)
            }
        }
    }

    data class ButtonData(val name: String, val image: Painter)
    @Composable
    fun BottomNavBar(navController: NavController, curNav: MutableState<String>) {
        var items = listOf<ButtonData>(
            ButtonData("Home", painterResource(R.drawable.home)),
            ButtonData("Insights", painterResource(R.drawable.insights)),
            ButtonData("NutriCoach", painterResource(R.drawable.nutricoach)),
            ButtonData("Settings", painterResource(R.drawable.settings))
        )

        NavigationBar {
            items.forEach { data ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            data.image,
                            data.name,
                            modifier = Modifier.Companion.size(25.dp)
                        )
                    },
                    label = {
                        Text(
                            data.name,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = Color.Companion.Blue,
                        selectedIconColor = Color.Companion.Blue,
                        indicatorColor = Color.Companion.LightGray
                    ),
                    selected = curNav.value == data.name,
                    onClick = {
                        navController.navigate(data.name)
                    }
                )
            }
        }
    }
}