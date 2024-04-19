package com.plcoding.storingapp

import android.os.Bundle
import android.util.Log
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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.plcoding.storingapp.Cabinets.AddCabinetScreen
import com.plcoding.storingapp.Camera.CameraPermission
import com.plcoding.storingapp.data.NotesDatabase
import com.plcoding.storingapp.Notes.AddNoteScreen
import com.plcoding.storingapp.Cabinets.CabinetViewModel
import com.plcoding.storingapp.Cabinets.MainScreen
import com.plcoding.storingapp.Cabinets.SearchScreenAtMS
import com.plcoding.storingapp.Cabinets.UpdateCabinetScreen
import com.plcoding.storingapp.Camera.CameraScreen
import com.plcoding.storingapp.Notes.NotesScreen
import com.plcoding.storingapp.Notes.NotesViewModel
import com.plcoding.storingapp.Notes.SearchScreen
import com.plcoding.storingapp.Notes.UpdateDataScreen
import com.plcoding.storingapp.ui.theme.StoringAppTheme

class MainActivity : ComponentActivity() {

    private val database by lazy{
        Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "storingDB.db"
        ).fallbackToDestructiveMigration().build()
    }

    private val NotesViewModel:NotesViewModel by viewModels<NotesViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(database.Notedao) as T
                }
            }
        }
    )

    private val CabinetViewModel: CabinetViewModel by viewModels<CabinetViewModel>(  // 新增的 CabinetViewModel
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val cabinetState by CabinetViewModel.state.collectAsState()
                    val noteState by NotesViewModel.state.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "MainScreen") {
                        composable("MainScreen"){
                            MainScreen(
                                state = cabinetState,
                                navController = navController,
                                onEvent = CabinetViewModel::onEvent,
                                viewModel= CabinetViewModel
                            )
                        }
                        composable("NotesScreen/{cabinetId}/{cabinetName}"){ backStackEntry ->
                            val cabinetId = backStackEntry.arguments?.getString("cabinetId")?:""
                            val cabinetName = backStackEntry.arguments?.getString("cabinetName")?:""
                            NotesScreen(
                                state = noteState,
                                navController = navController,
                                onEvent = NotesViewModel::onEvent,
                                cabinetId,
                                cabinetName
                            )
                        }
                        composable("AddCabinetScreen"){
                            AddCabinetScreen(
                                state = cabinetState,
                                navController = navController,
                                onEvent = CabinetViewModel::onEvent
                            )
                        }
                        composable("AddNoteScreen/{cabinetId}"){backStackEntry ->
                            val cabinetId = backStackEntry.arguments?.getString("cabinetId")?:""
                            AddNoteScreen(
                                state = noteState,
                                navController = navController,
                                onEvent = NotesViewModel::onEvent,
                                viewModel = NotesViewModel,
                                cabinetId
                            )
                        }
                        composable("SearchScreenAtMS"){
                            SearchScreenAtMS(
                                noteState = noteState,
                                cabinetState = cabinetState,
                                navController = navController,
                                onEvent = NotesViewModel::onEvent,
                                viewModel = NotesViewModel
                            )
                        }
                        composable("SearchScreen/{cabinetId}"){backStackEntry ->
                            val cabinetId = backStackEntry.arguments?.getString("cabinetId")?:""
                            SearchScreen(
                                state = noteState,
                                navController = navController,
                                onEvent = NotesViewModel::onEvent,
                                cabinetId = cabinetId
                            )
                        }
                        composable("UpdateDataScreen/{id}/{title}/{description}/{dateAdded}/{cabinetId}/{noteAmount}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            val title = backStackEntry.arguments?.getString("title") ?: ""
                            val description = backStackEntry.arguments?.getString("description") ?: ""
                            val dateAdded = backStackEntry.arguments?.getString("dateAdded") ?: ""
                            val cabinetId = backStackEntry.arguments?.getString("cabinetId") ?: ""
                            val noteAmount = backStackEntry.arguments?.getString("noteAmount") ?: ""
                            UpdateDataScreen(
                                state = noteState,
                                id,
                                title,
                                description,
                                dateAdded,
                                cabinetId,
                                noteAmount,
                                navController = navController,
                                onEvent = NotesViewModel::onEvent
                            )
                        }
                        composable("UpdateCabinetScreen/{id}/{cabinetName}/{cabinetDescription}/{dateAddedCabinet}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            val cabinetName = backStackEntry.arguments?.getString("cabinetName") ?: ""
                            val cabinetDescription = backStackEntry.arguments?.getString("cabinetDescription") ?: ""
                            val dateAddedCabinet = backStackEntry.arguments?.getString("dateAddedCabinet") ?: ""
                            UpdateCabinetScreen(
                                id,
                                cabinetName,
                                cabinetDescription,
                                dateAddedCabinet,
                                navController = navController,
                                onEvent = CabinetViewModel::onEvent
                            )
                        }
                        composable("CameraPermission"){
                            CameraPermission(
                                navController = navController,
                                viewModel = NotesViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}