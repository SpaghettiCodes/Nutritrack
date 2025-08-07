package com.example.chua_33520879.presentation.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chua_33520879.R
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.presentation.viewmodel.QuestionnaireViewModel
import com.example.chua_33520879.presentation.viewmodel.UploadState
import com.example.chua_33520879.ui.theme.Chua_33520879Theme

class QuestionnaireActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: QuestionnaireViewModel = ViewModelProvider(
            this, QuestionnaireViewModel.QuestionnaireViewModelFactory(this@QuestionnaireActivity)
        ) [QuestionnaireViewModel::class.java]
        viewModel.getUserData(AuthManager.getUserID()!!)

        setContent {
            Chua_33520879Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { Header() }
                ) { innerPadding ->
                    Questionnaire(modifier = Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Header() {
        TopAppBar (
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    "Food Intake Questionnaire",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        startActivity(Intent(this@QuestionnaireActivity, LoginActivity::class.java))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Questionnaire(modifier: Modifier, viewModel: QuestionnaireViewModel) {
        val userID = AuthManager.getUserID()
        var foodCategoryList = viewModel.foodCategoryList.collectAsStateWithLifecycle()
        var persona = viewModel.persona.collectAsStateWithLifecycle()
        var biggestMealTiming = viewModel.biggestMealTiming.collectAsStateWithLifecycle()
        var sleepTiming = viewModel.sleepTiming.collectAsStateWithLifecycle()
        var wakeUpTiming = viewModel.wakeUpTiming.collectAsStateWithLifecycle()
        var uploadState = viewModel.state.collectAsStateWithLifecycle()
        var context = LocalContext.current

        when (uploadState.value) {
            is UploadState.Success -> {
                viewModel.resetState()
                Toast.makeText(context, "Questionnaire Recorded!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@QuestionnaireActivity, MainActivity::class.java))
            }
            is UploadState.Error -> {
                val msg = (uploadState.value as UploadState.Error).msg
                viewModel.resetState()
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }
            else -> {

            }
        }

        val scrollState = rememberScrollState()

        Column (
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(horizontal = 15.dp)
        ) {
            Text (
                "Tick all the food categories you can eat",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.fillMaxWidth()
            )

            val foodCategories = listOf<CheckBoxData>(
                CheckBoxData("Fruits", "fruits"),
                CheckBoxData("Vegetables", "vegetables"),
                CheckBoxData("Grains", "grains"),
                CheckBoxData("Red Meat", "redmeat"),
                CheckBoxData("Seafood", "seafood"),
                CheckBoxData("Poultry", "poultry"),
                CheckBoxData("Fish", "fish"),
                CheckBoxData("Eggs", "eggs"),
                CheckBoxData("Nuts/Seeds", "nuts&seeds"),
            )
            CheckboxCollection(
                foodCategories,
                foodCategoryList.value,
                { viewModel.addFoodCategoryItem(it) },
                { viewModel.removeFoodCategoryItem(it) }
            )

            val personas = listOf<ModalData>(
                ModalData(
                    "Health Devotee",
                    "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy.",
                    painterResource(R.drawable.persona_1)
                ),
                ModalData(
                    "Mindful Eater",
                    "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media.",
                    painterResource(R.drawable.persona_2)
                ),
                ModalData(
                    "Wellness Striver",
                    "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.",
                    painterResource(R.drawable.persona_3)
                ),
                ModalData(
                    "Balance Seeker",
                    "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",
                    painterResource(R.drawable.persona_4)
                ),
                ModalData(
                    "Health Procrastinator",
                    "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life.",
                    painterResource(R.drawable.persona_5)
                ),
                ModalData(
                    "Food Carefree",
                    "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat.",
                    painterResource(R.drawable.persona_6)
                ),
            )
            Text (
                "Your Persona",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            )
            Text (
                "People can be broadly classfiied into 6 different types based on their eating preferences." +
                "Click on each button below to find out the different types, and select the type that best fits you!"
            )
            PersonPreviewList(personas)
            Text(
                "Which persona best fits you?",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            )
            DropdownMenu(personas, persona.value, { viewModel.updatePersona(it) })

            Text("Timings",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 5.dp)
            )

            val timingPrompts = listOf<Triple<String, String, (String) -> Unit>>(
                Triple("What time of day approx. do you normally eat your biggest meal?", biggestMealTiming.value, { viewModel.updateBiggestMealTiming(it) }),
                Triple("What time of day approx. do you go to sleep at night?", sleepTiming.value, { viewModel.updateSleepTiming(it) }),
                Triple("What time of day approx. do you wake up in the morning?", wakeUpTiming.value, { viewModel.updateWakeUpTiming(it) })
            )

            timingPrompts.forEach { prompt ->
                TimePickerCombo(prompt.first, prompt.second, prompt.third)
            }

            Button (
                onClick = {
                    viewModel.uploadFoodIntake(AuthManager.getUserID()!!)
                },
                modifier = Modifier
                    .width(150.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.save),
                        contentDescription = "save",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 10.dp),
                    )
                    Text(
                        "Save"
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DropdownMenu(
        options: List<ModalData>,
        userSelection: String,
        onSelect: (String) -> Unit
    ) {
        var dropDownIsOpen by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox (
            expanded = dropDownIsOpen,
            onExpandedChange = { dropDownIsOpen = it },
        ) {
            OutlinedTextField(
                value = userSelection,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select Option") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownIsOpen)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(25.dp)
            )

            ExposedDropdownMenu (
                expanded = dropDownIsOpen,
                onDismissRequest = { dropDownIsOpen = false },
                shape = RoundedCornerShape(25.dp)
            ) {
                options.forEach { value ->
                    DropdownMenuItem(
                        text = {
                            Text(value.title)
                        },
                        onClick = {
                            onSelect(value.title)
                            dropDownIsOpen = false
                        }
                    )
                }
            }
        }
    }

    data class CheckBoxData(
        var name: String,
        var repr: String
    )
    @Composable
    fun CheckboxCollection(
        list: List<CheckBoxData>,
        userData: List<String>,
        onCheck: (String) -> Unit,
        onUncheck: (String) -> Unit
    ) {
        return LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .padding(0.dp)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            items (list) { value ->
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Checkbox(
                        checked = userData.contains(value.repr),
                        onCheckedChange = {
                            if (it) {
                                onCheck(value.repr)
                            }
                            else {
                                onUncheck(value.repr)
                            }
                        },
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(20.dp)
                    )
                    Text(
                        value.name,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

    @Composable
    fun PersonPreviewList(list: List<ModalData>) {
        return LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .height(120.dp)
                .padding(0.dp)
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            items (list) { value ->
                ShowButtonAndModal (
                    value
                )
            }
        }
    }

    @Composable
    fun timePickerComponent(
        onInput: (String) -> Unit
    ): TimePickerDialog {
        val mContext = LocalContext.current
        val mCalender = Calendar.getInstance()

        val mHour = mCalender.get(Calendar.HOUR_OF_DAY)
        val mMinute = mCalender.get(Calendar.MINUTE)

        mCalender.time = Calendar.getInstance().time

        return TimePickerDialog(
            mContext,
            { _, mHour: Int, mMinute: Int ->
                onInput(String.format("%02d:%02d", mHour, mMinute))
            }, mHour, mMinute, false
        )
    }

    @Composable
    fun TimePickerCombo (
        prompt: String,
        userValue: String,
        onInput: (String) -> Unit
    ) {
        var timePickerDialog = timePickerComponent(onInput)

        Row (
            modifier = Modifier.padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                prompt,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(2.5f)
            )
            TextButton (
                onClick = { timePickerDialog.show() },
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = ButtonColors(Color.White, Color.Black, Color.White, Color.Black),
                modifier = Modifier
                    .width(125.dp)
                    .weight(1f)
            ) {
                Row (
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.clock),
                        contentDescription = "Clock",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 10.dp),
                    )
                    val color = if (userValue.isEmpty()) Color.LightGray else Color.Black
                    val value = if (userValue.isEmpty()) "00:00" else userValue
                    Text(
                        value,
                        color = color
                    )
                }
            }
        }
    }

    data class ModalData(
        val title: String,
        val content: String,
        val image: Painter,
    )
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowButtonAndModal(data: ModalData) {
        var showDialog by remember { mutableStateOf(false) }
        Button(
            onClick = { showDialog = true },
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Text (
                data.title,
                fontSize = 12.sp
            )
        }
        if (showDialog) {
            BasicAlertDialog (
                onDismissRequest = { showDialog = false },
            ) {
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.White)
                        .padding(vertical = 20.dp, horizontal = 35.dp),
                ) {
                    Image(
                        data.image,
                        data.title,
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        data.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Text(
                        data.content,
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { showDialog = false },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .size(135.dp, 55.dp)
                            .padding(top = 15.dp)
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}
