package com.example.chua_33520879.presentation.composable

import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.chua_33520879.presentation.viewmodel.PatientViewModel
import kotlin.math.roundToInt

@Composable
fun InsightPage(navController: NavController, viewModel: PatientViewModel) {
    var userID = AuthManager.getUserID()!!
    var userData = viewModel.getUserData(userID).collectAsStateWithLifecycle(Patient())
    var context = LocalContext.current
    var scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(scrollState)
            .heightIn(min = 450.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            "Insights: Food Score",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            TotalFoodQualityScore(userData.value)
            Column (
                modifier = Modifier
                    .weight(1f, fill = false)
            ) {
                Text(
                    "Score Breakdown",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
                FoodQualityScores(userData.value, Modifier.weight(1f, fill = false))
            }
            ShareButton {
                val shareIntent = Intent(ACTION_SEND)
                val data = "Hi, I just got a HEIFA score of " + userData.value.HEIFATotalScore + "!"
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, data)
                context.startActivity(Intent.createChooser(shareIntent, "Share results via: "))
            }
            ImproveDietButton {
                navController.navigate("NutriCoach")
            }
        }
    }
}

data class FoodDetails(val name: String, val maxScore: Float, val value: Float)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodQualityScores(userData: Patient, modifier: Modifier) {
    var foods = listOf<FoodDetails>(
        FoodDetails("Vegetables", 5f, userData.VegetablesHEIFAScore),
        FoodDetails("Fruits", 10f, userData.FruitHEIFAScore),
        FoodDetails("Grains & Cereals", 5f, userData.GrainsAndCerealsHEIFAScore),
        FoodDetails("Whole Grains", 5f, userData.WholeGrainsHEIFAScore),
        FoodDetails("Meat & Alternatives", 10f, userData.MeatAndAlternativesHEIFAScore),
        FoodDetails("Dairy & Alternatives", 10f, userData.DairyAndAlternativesHEIFAScore),
        FoodDetails("Water", 5f, userData.WaterHEIFAScore),
        FoodDetails("Saturated Fats", 5f, userData.SaturatedFatHEIFAScore),
        FoodDetails("Unsaturated Fats", 5f, userData.UnsaturatedFatHEIFAScore),
        FoodDetails("Sodium", 10f, userData.SodiumHEIFAScore),
        FoodDetails("Sugar", 10f, userData.SugarHEIFAScore),
        FoodDetails("Alcohol", 5f, userData.AlcoholHEIFAScore),
        FoodDetails("Discretionary Foods", 10f, userData.DiscretionaryHEIFAScore),
    )

    val scrollState = rememberScrollState()
    Column (
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        foods.forEach { data ->
            Column (
                modifier = Modifier.height(35.dp),
            )
            {
                Text (
                    data.name,
                    modifier = Modifier.weight(1f),
                    fontSize = 12.sp,
                    lineHeight = 15.sp
                )
                Row (
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = data.value,
                        onValueChange = {  },
                        enabled = false,
                        valueRange = 0f..data.maxScore,
                        modifier = Modifier.weight(1.5f),
                        thumb = {

                        },
                        track = {
                                sliderState ->

                            // Calculate fraction of the slider that is active
                            val fraction by remember {
                                derivedStateOf {
                                    (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                                }
                            }

                            Box(Modifier.fillMaxWidth()) {
                                Box(
                                    Modifier
                                        .fillMaxWidth(fraction)
                                        .align(Alignment.CenterStart)
                                        .height(15.dp)
                                        .background(
                                            Color(102, 102, 255, 255),
                                            RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                                        )
                                )
                                Box(
                                    Modifier
                                        .fillMaxWidth(1f - fraction)
                                        .align(Alignment.CenterEnd)
                                        .height(15.dp)
                                        .background(
                                            Color(200, 200, 255, 255),
                                            RoundedCornerShape(bottomEnd = 5.dp, topEnd = 5.dp)
                                        )
                                )
                            }
                        }
                    )
                    Text(
                        "${data.value}/${data.maxScore.roundToInt()}",
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(0.3f),
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalFoodQualityScore(userData: Patient) {
    Column (
        modifier = Modifier
    )
    {
        Text(
            "Total Food Quality Score",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.height(35.dp)
        ) {
            Slider(
                value = userData.HEIFATotalScore,
                onValueChange = { },
                enabled = false,
                valueRange = 0f..100f,
                modifier = Modifier.weight(1f).padding(end = 15.dp),
                thumb = {

                },
                track = {
                        sliderState ->

                    // Calculate fraction of the slider that is active
                    val fraction by remember {
                        derivedStateOf {
                            (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                        }
                    }

                    Box(Modifier.fillMaxWidth()) {
                        Box(
                            Modifier
                                .fillMaxWidth(fraction)
                                .align(Alignment.CenterStart)
                                .height(20.dp)
                                .background(
                                    Color(102, 102, 255, 255),
                                    RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                                )
                        )
                        Box(
                            Modifier
                                .fillMaxWidth(1f - fraction)
                                .align(Alignment.CenterEnd)
                                .height(20.dp)
                                .background(
                                    Color(200, 200, 255, 255),
                                    RoundedCornerShape(bottomEnd = 5.dp, topEnd = 5.dp)
                                )
                        )
                    }
                }
            )
            Text(
                "${userData.HEIFATotalScore}/100",
                textAlign = TextAlign.End,
            )
        }
    }
}

@Composable
fun ShareButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Icon(
                Icons.Default.Share,
                contentDescription = "Share"
            )
            Text("Share with someone")
        }
    }
}

@Composable
fun ImproveDietButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(
                    R.drawable.improve_diet,
                ),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "NutriCoach",
                modifier = Modifier.size(20.dp)
            )
            Text("Improve my diet!")
        }
    }
}
