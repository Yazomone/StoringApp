package com.plcoding.storingapp.Camera

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.plcoding.storingapp.Notes.NotesViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(
    navController: NavController,
    viewModel: NotesViewModel
) {

    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    MainContent(
        navController = navController,
        viewModel = viewModel,
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest
    )
}
@Composable
private fun MainContent(
    navController: NavController,
    hasPermission: Boolean,
    viewModel: NotesViewModel,
    onRequestPermission: () -> Unit
) {

    if (hasPermission) {
        CameraScreen(navController,viewModel)
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}
