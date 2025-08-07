package com.example.chua_33520879.presentation.composable

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chua_33520879.R
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.data.patient.Patient
import com.example.chua_33520879.presentation.activity.QuestionnaireActivity
import com.example.chua_33520879.presentation.viewmodel.PatientViewModel
import kotlin.math.roundToInt

@Composable
fun HomePage(navController: NavController, viewModel: PatientViewModel) {
    val userID = AuthManager.getUserID()
    val userData = viewModel.getUserData(userID!!).collectAsStateWithLifecycle(Patient())
    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .padding(15.dp)
            .verticalScroll(scrollState)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column {
            Text(
                "Hello,",
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                userData.value.name,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                "You've already filled in your Food Intake Questionnaire, but you can change details here:",
                modifier = Modifier
                    .weight(5.5f, fill = false),
                fontSize = 12.sp,
                lineHeight = 15.sp
            )
            val context = LocalContext.current
            Button(
                onClick = { context.startActivity(Intent(
                    context,
                    QuestionnaireActivity::class.java
                )) },
                modifier = Modifier.weight(2.5f),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape (5.dp)
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Create,
                        contentDescription = "Edit",
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        "Edit",
                        modifier = Modifier
                    )
                }
            }
        }
        Image(
            painterResource(
                R.drawable.fooddish,
            ),
            contentDescription = "The Food Plate",
            modifier = Modifier.size(300.dp)
        )
        Score(userData.value.HEIFATotalScore, navController)
        FoodQualityQNA()
    }
}

@Composable
fun Score(totalScore: Float, navController: NavController) {
    Column {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "My Score",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            TextButton (
                onClick = { navController.navigate("Insights") },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "See all scores"
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Left Arrow"
                )
            }
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row (
                modifier = Modifier.weight(8f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface (
                    shape = CircleShape,
                    modifier = Modifier.padding(end = 15.dp)
                ) {
                    Image(
                        painterResource(
                            R.drawable.up_arrow,
                        ),
                        contentDescription = "Up arrow",
                        modifier = Modifier
                            .size(35.dp)
                            .background(Color.LightGray)
                            .padding(10.dp)
                            .rotate(if (totalScore >= 50f) 0f else 180f)
                    )
                }
                Text(
                    "Your Food Quality score",
                )
            }
            Text(
                String.format("%d/100", totalScore.roundToInt()),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.W900,
                color = Color(0, 155, 0, 255),
                modifier = Modifier.weight(2f)
            )
        }
    }
}

@Composable
fun FoodQualityQNA() {
    var description = "Your Food Quality Score provides a snapshot of how well your " +
            "eating patterns align with established food guidelines, helping " +
            "you identify both strengths and opportunities for improvement " +
            "in your diet."
    var description2 = "This personalized measurement considers various food groups " +
            "including vegetables, fruits, whole grains, and proteins to give " +
            "you practical insights for making healthier food choices. "

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "What is the Food Quality Score?",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
        Text(
            description,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            description2,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}