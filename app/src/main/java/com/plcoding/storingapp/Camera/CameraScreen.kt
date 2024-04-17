package com.plcoding.storingapp.Camera

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.plcoding.storingapp.Notes.NotesViewModel

@Composable
fun CameraScreen(
    navController: NavController,
    viewModel: NotesViewModel
) {
    CameraContent(
        navController = navController,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CameraContent(
    navController: NavController,
    viewModel: NotesViewModel
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }
    var detectedText: String by remember { mutableStateOf("No text detected yet..") }

    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
        viewModel.updateDisplayText(detectedText)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Text Scanner") }) },
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        startTextRecognition(
                            context = context,
                            cameraController = cameraController,
                            lifecycleOwner = lifecycleOwner,
                            previewView = previewView,
                            onDetectedTextUpdated = ::onTextUpdated
                        )
                    }
                }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(androidx.compose.ui.graphics.Color.Black)
            ) {
                val displayText = if (detectedText.length > 50) "文字過多無法讀取" else detectedText

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = displayText,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { detectedText = "No text detected yet.." }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete Text",
                        )
                    }

                    IconButton(onClick = {
                        onTextUpdated(detectedText)
                        navController.popBackStack()
                    }){
                        Icon(imageVector = Icons.Rounded.Check,
                            contentDescription = "Save Note"
                        )
                    }
                }
            }
        }

    }
}

private fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextUpdated: (String) -> Unit
) {

    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_4_3)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer( onDetectedTextUpdated = onDetectedTextUpdated)
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}