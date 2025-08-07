package com.example.chua_33520879.presentation.composable

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.chua_33520879.data.ai.FoodAnalysisResult
import com.example.chua_33520879.presentation.viewmodel.AiState
import com.example.chua_33520879.presentation.viewmodel.CalorieTrackerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FoodAnalysisPage (
    navController: NavController,
    caloreTrackerVM: CalorieTrackerViewModel
) {
    val state = caloreTrackerVM.aiState.collectAsStateWithLifecycle()

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(key1 = Unit) {
        if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
    var scrollState = rememberScrollState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(scrollState)
            .heightIn(min = 450.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Button (
                onClick = { navController.popBackStack() },
                contentPadding = PaddingValues()
            ) {
                Icon(Icons.Filled.ArrowBack, null)
            }
            Text(
                "Food Analysis",
                fontWeight = FontWeight.Companion.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                textAlign = TextAlign.Center
            )
        }

        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cameraPermissionState.status.isGranted) {
                CameraPreview(
                    Modifier
                        .fillMaxSize()
                        .weight(1f, fill = false),
                    cameraController,
                    lifecycleOwner
                )
            } else
                NoCameraPermissionScreen(cameraPermissionState)
        }

        var innerScrollState = rememberScrollState()
        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column (
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxSize()
                    .verticalScroll(innerScrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (state.value) {
                    is AiState.Error -> {
                        Text(
                            (state.value as AiState.Error).errorMessage,
                            color = Color.Red, textAlign = TextAlign.Center
                        )
                    }
                    AiState.Initial -> {
                        Text("Waiting for you to take a picture...")
                    }
                    AiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is AiState.Success<*> -> {
                        val res = ((state.value as AiState.Success<*>).outputText as FoodAnalysisResult)
                        AnalysisResult(res)
                    }
                }
            }

            if (cameraPermissionState.status.isGranted) {
                ShutterButton(
                    context,
                    cameraController
                ) {
                    caloreTrackerVM.analyzeImage(it)
                }
            }
        }
    }
}

@Composable
fun AnalysisResult(data: FoodAnalysisResult) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(data.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        DetailRow("Calories", data.calories.toString())
        DropDown("Ingredients", data.ingredients)
        DropDown("Nutrition Facts", data.nutrition)

        Column (
            modifier = Modifier
                .fillMaxSize()
                .border(
                    BorderStroke(1.dp, Color.LightGray),
                    RoundedCornerShape(5.dp)
                )
                .padding(5.dp)
        ) {
            Text("Recommendation")
            Row (
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RecommendationBar(data.recommend.toFloat(), modifier = Modifier.weight(1.5f))
                Text(data.recommend.toString() + " / 10")
            }
        }
        DropDown("Why?", data.reasons)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationBar(value: Float, modifier: Modifier) {
    Slider(
        value = value,
        onValueChange = {  },
        enabled = false,
        valueRange = 0f..10f,
        modifier = modifier,
        thumb = {
        },
        track = { sliderState ->
            SliderDefaults.Track(
                sliderState,
                Modifier
                    .height(15.dp)
                    .clip(RoundedCornerShape(5.dp)),
                colors = SliderDefaults.colors(
                    activeTrackColor = Color(102, 102, 255, 255),
                    inactiveTrackColor = Color(200, 200, 255, 255)
                ),
                thumbTrackGapSize = 0.dp,
                trackInsideCornerSize = 0.dp,
                drawStopIndicator = {   }
            )
        }
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoCameraPermissionScreen(cameraPerms: PermissionState) {
    Column (
        modifier = Modifier
            .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(5.dp))
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No camera permission granted, click below to grant permission", textAlign = TextAlign.Center)
        Button(
            onClick = { cameraPerms.launchPermissionRequest() }
        ) {
            Text("Grant Camera Permissions")
        }
    }
}

@Composable
fun DropDown(
    label: String,
    data: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(1.dp, Color.LightGray),
                    RoundedCornerShape(5.dp)
                )
                .padding(5.dp)
                .clickable() {
                    expanded = !expanded
                }
        ) {
            Text(label, modifier = Modifier
                .weight(1f, fill = false)
                .fillMaxWidth())
            Icon(
                if (expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                null
            )
        }

        if (expanded) {
            data.forEach { value ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(1.dp, Color.DarkGray),
                            RoundedCornerShape(5.dp)
                        )
                        .background(Color.LightGray)
                        .padding(5.dp)
                ) {
                    Text(value, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                setBackgroundColor(Color.White.toArgb())
                scaleType = PreviewView.ScaleType.FIT_CENTER
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }.also { previewView ->
                previewView.controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner)
            }
        }, onRelease = {
            cameraController.unbind()
        }
    )

}

@Composable
fun ShutterButton(
    context: Context,
    cameraController: LifecycleCameraController,
    onPictureTaken: (Image: Bitmap) -> Unit
) {
    Button(
        onClick = {
            val mainExecutor = ContextCompat.getMainExecutor(context)
            cameraController.takePicture(mainExecutor, object: ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    onPictureTaken(image.toBitmap())
                    image.close()
                }
            })
        }
    ) {
        Text("Take a Picture")
    }
}