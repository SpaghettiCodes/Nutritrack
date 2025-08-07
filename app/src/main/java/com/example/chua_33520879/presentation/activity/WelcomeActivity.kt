package com.example.chua_33520879.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chua_33520879.R
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.data.patient.csvProcessor
import com.example.chua_33520879.ui.theme.Chua_33520879Theme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Chua_33520879Theme {
                AuthManager.initialize(
                    LocalContext.current.getSharedPreferences(
                        AuthManager.sharedPrefName,
                        Context.MODE_PRIVATE
                    )
                )
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                   MainPage(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun Disclaimer () {
        return Text(
            text = "Designed with ❤\uFE0F by Chua Shi Xiang (33520879)",
            fontSize = 12.sp
        )
    }


    @Composable
    fun MainPage(modifier: Modifier) {
        return Column (
            modifier = modifier.fillMaxSize().padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column (
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "NutriTrack",
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                )
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Nutritrack Logo",
                    modifier = Modifier.size(150.dp).padding(bottom = 25.dp, top = 15.dp)
                )
                Text(
                    text = "This app provides general health and nutrition information for " +
                            "educational purposes only. It is not intended as medical advice, " +
                            "diagnosis, or treatment. Always consult a qualified healthcare " +
                            "professional before making any changes to your diet, exercise, or " +
                            "health regimen. Use this app at your own risk. " +
                            "If you’d like to an Accredited Practicing Dietitian (APD), please " +
                            "visit the Monash Nutrition/Dietetics Clinic (discounted rates for students): ",
                    modifier = Modifier.padding(25.dp, 0.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 20.sp
                )
                val uriHandler = LocalUriHandler.current
                val uri = "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition"
                Text(
                    text = uri,
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.clickable(
                        onClick = {
                            uriHandler.openUri(uri)
                        }
                    ),
                    lineHeight = 20.sp
                )
            }

            Button(
                onClick = {
                    if (AuthManager.isLoggedIn()) {
                        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    } else {
                        startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(15.dp)
            ) {
                Text(
                    text = "Login",
                    fontSize = 15.sp
                )
            }

            Disclaimer()
        }
    }
}
