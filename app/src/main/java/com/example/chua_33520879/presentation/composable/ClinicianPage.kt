package com.example.chua_33520879.presentation.composable

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chua_33520879.data.ai.AIRecord
import com.example.chua_33520879.presentation.viewmodel.AiState
import com.example.chua_33520879.presentation.viewmodel.ClinicianViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ClinicianPage(navController: NavController, viewModel: ClinicianViewModel) {
    val maleAvg = viewModel.maleAvg.collectAsStateWithLifecycle(0f)
    val femaleAvg = viewModel.femaleAvg.collectAsStateWithLifecycle(0f)
    val aiState = viewModel.aiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(scrollState)
            .heightIn(550.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Clinician Dashboard",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 25.dp)
        )

        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AverageHEIFARow("Average HEIFA (Male)", "%.2f".format(maleAvg.value))
            AverageHEIFARow("Average HEIFA (Female)", "%.2f".format(femaleAvg.value))
        }

        HorizontalDivider(color = Color.Gray, modifier = Modifier.padding(vertical = 25.dp))

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f).heightIn(250.dp)
        ) {
            Button(
                onClick = {
                    viewModel.analyzeData()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonColors(
                    Color.Companion.Blue,
                    Color.Companion.White,
                    Color.Companion.Blue,
                    Color.Companion.White
                ),
                contentPadding = PaddingValues(10.dp)
            ) {
                Icon(
                    Icons.Filled.Search,
                    null,
                    modifier = Modifier.padding(end = 10.dp).size(25.dp)
                )
                Text("Find Data Pattern", fontSize = 17.sp)
            }

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                when (aiState.value) {
                    is AiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is AiState.Initial -> {
                        Text("Click on the button above to find patterns in the database!", textAlign = TextAlign.Center)
                    }
                    is AiState.Error -> {
                        Text((aiState.value as AiState.Error).errorMessage, color = Color.Red, textAlign = TextAlign.Center)
                    }
                    is AiState.Success<*> -> {
                        val output = (aiState.value as AiState.Success<*>).outputText
                        AnalyzeResult(output as List<Pair<String, String>>)
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.updateKey("")
                    navController.navigate("Settings")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonColors(
                    Color.Companion.Blue,
                    Color.Companion.White,
                    Color.Companion.Blue,
                    Color.Companion.White
                ),
                contentPadding = PaddingValues(10.dp)
            ) {
                Icon(
                    Icons.Filled.ExitToApp,
                    null,
                    modifier = Modifier.padding(end = 10.dp).size(25.dp)
                )
                Text("Done", fontSize = 17.sp)
            }
        }
    }
}

@Composable
fun AverageHEIFARow(label: String, value: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
    ) {
        Text(label, modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
        Text(":", modifier = Modifier.padding(horizontal = 15.dp))
        Text(value, modifier = Modifier.weight(1f))
    }
}

@Composable
fun AnalyzeResult(data: List<Pair<String, String>>) {
    LazyColumn (
        modifier = Modifier
            .height(350.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(data) { value ->
            Card (
                border = BorderStroke(1.dp, Color.Gray),
                colors = CardDefaults.cardColors().copy(
                    containerColor = Color.Transparent
                )
            ) {
                Column (
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(value.first, fontWeight = FontWeight.Bold)
                    HorizontalDivider(color = Color.Gray)
                    Text(value.second)
                }
            }
        }
    }
}
