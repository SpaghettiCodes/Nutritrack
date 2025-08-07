package com.example.chua_33520879.presentation.composable

import androidx.annotation.ColorRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.Lucide
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.data.fruityvice.FruitResponseModel
import com.example.chua_33520879.presentation.viewmodel.NutricoachViewModel
import com.example.chua_33520879.presentation.viewmodel.PatientViewModel
import com.example.chua_33520879.R
import com.example.chua_33520879.data.ai.AIRecord
import com.example.chua_33520879.presentation.viewmodel.AiState
import com.example.chua_33520879.presentation.viewmodel.ApiState
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun NutriCoachPage(
    navController: NavController,
    patientViewModel: PatientViewModel,
    nutricoachViewModel: NutricoachViewModel
) {
    val userID = AuthManager.getUserID()
    val apiState = nutricoachViewModel.apiState.collectAsStateWithLifecycle()
    val aiState = nutricoachViewModel.aiState.collectAsStateWithLifecycle()
    val fruit = nutricoachViewModel.fruit.collectAsStateWithLifecycle()
    val history = patientViewModel.getAIHistory(userID!!).collectAsStateWithLifecycle(emptyList())
    val optimalFruit = patientViewModel.optimalFruit.collectAsStateWithLifecycle()
    var switchToAPI by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val modalState = nutricoachViewModel.showAllModal.collectAsStateWithLifecycle()

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
            "NutriCoach",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )

        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            var isLoading by remember { mutableStateOf(false) }
            var loadError by remember { mutableStateOf(false) }

            when (!optimalFruit.value || switchToAPI) {
                false -> {
                    Column (
                        modifier = Modifier
                            .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(15.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column (
                            modifier = Modifier.weight(1f, fill = false).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Your Fruit Intake is Optimal!")
                            if (isLoading) {
                                CircularProgressIndicator()
                            }
                            AsyncImage(
                                model = "https://picsum.photos/300",
                                contentDescription = "random picture",
                                modifier = Modifier.fillMaxHeight().clip(RoundedCornerShape(15.dp)),
                                onLoading = { isLoading = true },
                                onSuccess = {
                                    isLoading = false
                                    loadError = false
                                },
                                onError = {
                                    isLoading = false
                                    loadError = true
                                }
                            )
                        }
                        Button (
                            onClick = { switchToAPI = true },
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text("Click here to search for information about fruits", textAlign = TextAlign.Center)
                        }
                    }
                }
                true -> {
                    FruitFinder(
                        fruit.value,
                        { nutricoachViewModel.setFruit(it) },
                        { nutricoachViewModel.getFruitDetails() },
                        apiState.value
                    )
                }
            }

        }
        HorizontalDivider(color = Color.Gray)
        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    nutricoachViewModel.generateMotivationalMessage(userID!!)
                },
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
                Icon(
                    painterResource(R.drawable.messages),
                    null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(25.dp)
                )
                Text("Motivational Message (AI)", fontSize = 17.sp)
            }

            val scrollState = rememberScrollState()
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                when (aiState.value) {
                    is AiState.Error -> {
                        Text((aiState.value as AiState.Error).errorMessage, color = Color.Red, textAlign = TextAlign.Center)
                    }
                    is AiState.Success<*> -> {
                        Text(
                            (aiState.value as AiState.Success<*>).outputText as String,
                            textAlign = TextAlign.Center,
                        )
                    }
                    is AiState.Initial -> {
                        Text("Generate a motivational message by pressing the button above!", textAlign = TextAlign.Center)
                    }
                    is AiState.Loading -> {
                        CircularProgressIndicator()
                    }
                }
            }

            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { navController.navigate("Food Analysis") },
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
                    Icon(Lucide.Camera, null, modifier = Modifier.padding(end = 15.dp))
                    Text("Food Analysis")
                }
                AllTipsModalScreen(history.value, modalState.value) {
                    nutricoachViewModel.setModal(it)
                }
            }
        }
    }
}

@Composable
fun FruitDetails(fruit: FruitResponseModel) {
    val scrollState = rememberScrollState()

    Column (
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        DetailRow("family", fruit.genus)
        DetailRow("calories", fruit.nutritions.calories.toString())
        DetailRow("fat", fruit.nutritions.fat.toString())
        DetailRow("sugar", fruit.nutritions.sugar.toString())
        DetailRow("carbohydrates", fruit.nutritions.carbohydrates.toString())
        DetailRow("protein", fruit.nutritions.protein.toString())
    }
}

@Composable
fun FruitFinder(
    fruitState: String,
    setFruitInput: (String) -> Unit,
    getFruitDetails: () -> Unit,
    apiState: ApiState
) {
    Text(
        "Fruit Name",
        fontWeight = FontWeight.Bold
    )
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        TextField(
            value = fruitState,
            onValueChange = setFruitInput,
            placeholder = {
                Text("Input your fruit here")
            },
            modifier = Modifier
                .weight(1.75f)
                .clip(RoundedCornerShape(15.dp))
                .border(
                    BorderStroke(1.dp, Color.Gray), // Specify border thickness, color, and shape
                    RoundedCornerShape(15.dp)
                ),
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            singleLine = true,
            shape = RoundedCornerShape(15.dp)
        )

        Button(
            onClick = getFruitDetails,
            modifier = Modifier.weight(1.0f),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonColors(
                Color.Companion.Blue,
                Color.Companion.White,
                Color.Companion.Blue,
                Color.Companion.White
            ),
            contentPadding = PaddingValues(10.dp)
        ) {
            Icon(Icons.Filled.Search, null)
            Text("Details", fontSize = 17.sp)
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 15.dp)
    ) {
        when (apiState) {
            is ApiState.Error -> {
                Text(apiState.errorMessage, color = Color.Red, textAlign = TextAlign.Center)
            }
            is ApiState.Success -> {
                FruitDetails(apiState.output)
            }
            is ApiState.Initial -> {
                Text("No Fruit Details Shown", textAlign = TextAlign.Center)
                Text("Search for a fruit using the search bar above!", textAlign = TextAlign.Center)
            }
            is ApiState.Loading -> {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color.LightGray),
                RoundedCornerShape(5.dp)
            )
            .padding(5.dp)
    ) {
        Text(label, modifier = Modifier.weight(0.75f))
        Text(":", modifier = Modifier.padding(horizontal = 15.dp))
        Text(value, modifier = Modifier.weight(1.5f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTipsModalScreen(aiHistory: List<AIRecord>, showDialog: Boolean, setShowDialog: (Boolean) -> Unit) {
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
        Icon(
            Icons.Filled.Email,
            null,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(25.dp)
        )
        Text("Show All Tips", fontSize = 17.sp)
    }

    if (showDialog) {
        BasicAlertDialog (
            onDismissRequest = { setShowDialog(false) },
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
                    "AI Tips",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 15.dp)
                )
                if (aiHistory.isNotEmpty()) {
                    LazyColumn (
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        modifier = Modifier
                            .heightIn(max = 550.dp)
                            .weight(1f, fill = false)
                    ) {
                        items(aiHistory) { data ->
                            Card (
                                colors = CardDefaults.cardColors().copy(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                            )
                            {
                                val localDateTime = data.createdAt.toInstant()
                                    .atZone(ZoneId.systemDefault()) // or ZoneId.of("Australia/Melbourne"), etc.
                                val formatted = localDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))

                                Column (
                                    modifier = Modifier.padding(15.dp)
                                ) {
                                    Text(formatted, fontSize = 15.sp, color = Color.Gray)
                                    Text(data.message)
                                }
                            }
                        }
                    }
                } else {
                    Text("Nothing to see here!")
                }
                Button(
                    onClick = {
                        setShowDialog(false)
                    },
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .align(Alignment.End)
                        .width(150.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonColors(
                        Color.Companion.Blue,
                        Color.Companion.White,
                        Color.Companion.Blue,
                        Color.Companion.White
                    ),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    Text("Done", fontSize = 17.sp)
                }
            }
        }
    }
}