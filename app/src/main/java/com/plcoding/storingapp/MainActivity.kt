package com.plcoding.storingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.plcoding.storingapp.Camera.CameraPermission
import com.plcoding.storingapp.data.NotesDatabase
import com.plcoding.storingapp.presentation.AddNoteScreen
import com.plcoding.storingapp.presentation.CabinetViewModel
import com.plcoding.storingapp.presentation.NotesScreen
import com.plcoding.storingapp.presentation.NotesViewModel
import com.plcoding.storingapp.presentation.SearchScreen
import com.plcoding.storingapp.presentation.UpdateDataScreen
import com.plcoding.storingapp.ui.theme.StoringAppTheme

class MainActivity : ComponentActivity() {


    private val database by lazy{
        Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "notes.db"
        ).build()
    }

    private val NotesviewModel:NotesViewModel by viewModels<NotesViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(database.Notedao) as T
                }
            }
        }
    )

    private val cabinetViewModel: CabinetViewModel by viewModels<CabinetViewModel>(  // 新增的 CabinetViewModel
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CabinetViewModel(database.CabinetDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoringAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by NotesviewModel.state.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "MainScreen") {
                        composable("MainScreen"){
                            NotesScreen(
                                state = state,
                                navController = navController,
                                onEvent = cabinetViewModel::onEvent
                            )
                        }
                        composable("NotesScreen"){
                            NotesScreen(
                                state = state,
                                navController = navController,
                                onEvent = NotesviewModel::onEvent
                            )
                        }
                        composable("AddNoteScreen"){

                            AddNoteScreen(
                                state = state,
                                navController = navController,
                                onEvent = NotesviewModel::onEvent
                            )
                        }
                        composable("SearchScreen"){

                            SearchScreen(
                                state = state,
                                navController = navController,
                                onEvent = NotesviewModel::onEvent
                            )
                        }
                        composable("UpdateDataScreen/{id}/{title}/{description}/{dateAdded}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            val title = backStackEntry.arguments?.getString("title") ?: ""
                            val description = backStackEntry.arguments?.getString("description") ?: ""
                            val dateAdded = backStackEntry.arguments?.getString("dateAdded") ?: ""
                            UpdateDataScreen(
                                id,
                                title,
                                description,
                                dateAdded,
                                state = state,
                                navController = navController,
                                onEvent = NotesviewModel::onEvent
                            )
                        }
                        composable("CameraPermission"){
                            CameraPermission()
                        }
                    }
                }
            }
        }
    }
}