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
import com.plcoding.storingapp.Cabinets.AddCabinetScreen
import com.plcoding.storingapp.Cabinets.CabinetScreen
import com.plcoding.storingapp.Camera.CameraPermission
import com.plcoding.storingapp.data.NotesDatabase
import com.plcoding.storingapp.Notes.AddNoteScreen
import com.plcoding.storingapp.Cabinets.CabinetViewModel
import com.plcoding.storingapp.Cabinets.SearchScreenAtMS
import com.plcoding.storingapp.Cabinets.UpdateCabinetScreen
import com.plcoding.storingapp.Main.MainScreen
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
                                navController = navController
                            )
                        }
                        composable("CabinetScreen"){
                            CabinetScreen(
                                state = cabinetState,
                                navController = navController,
                                onEvent = CabinetViewModel::onEvent,
                                viewModel= CabinetViewModel
                            )
                        }
                        composable("NotesScreen/{cabinetId}/{cabinetName}/{cabinetDescription}"){ backStackEntry ->
                            val cabinetId = backStackEntry.arguments?.getString("cabinetId")?:""
                            val cabinetName = backStackEntry.arguments?.getString("cabinetName")?:""
                            val cabinetDescription = backStackEntry.arguments?.getString("cabinetDescription")?:""
                            NotesScreen(
                                state = noteState,
                                navController = navController,
                                onEvent = NotesViewModel::onEvent,
                                cabinetId,
                                cabinetName,
                                cabinetDescription
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
                        composable("SearchScreen/{cabinetId}/{cabinetName}"){backStackEntry ->
                            val cabinetId = backStackEntry.arguments?.getString("cabinetId")?:""
                            val cabinetName = backStackEntry.arguments?.getString("cabinetName")?:""
                            SearchScreen(
                                state = noteState,
                                navController = navController,
                                onEvent = NotesViewModel::onEvent,
                                cabinetName = cabinetName,
                                cabinetId = cabinetId
                            )
                        }
                        composable("UpdateDataScreen/{id}/{title}/{description}/{dateAdded}/{cabinetId}/{noteAmount}/{cabinetName}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            val title = backStackEntry.arguments?.getString("title") ?: ""
                            val description = backStackEntry.arguments?.getString("description") ?: ""
                            val dateAdded = backStackEntry.arguments?.getString("dateAdded") ?: ""
                            val cabinetId = backStackEntry.arguments?.getString("cabinetId") ?: ""
                            val noteAmount = backStackEntry.arguments?.getString("noteAmount") ?: ""
                            val cabinetName = backStackEntry.arguments?.getString("cabinetName")?:""
                            UpdateDataScreen(
                                state = noteState,
                                id,
                                title,
                                description,
                                dateAdded,
                                cabinetId,
                                noteAmount = noteAmount,
                                cabinetName = cabinetName,
                                navController = navController,
                                onEvent = NotesViewModel::onEvent,
                                viewModel = NotesViewModel
                            )
                        }
                        composable("UpdateCabinetScreen/{id}/{cabinetName}/{cabinetDescription}/{dateAddedCabinet}/{isFavorite}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            val cabinetName = backStackEntry.arguments?.getString("cabinetName") ?: ""
                            val cabinetDescription = backStackEntry.arguments?.getString("cabinetDescription") ?: ""
                            val dateAddedCabinet = backStackEntry.arguments?.getString("dateAddedCabinet") ?: ""
                            val isFavorite = backStackEntry.arguments?.getString("isFavorite") ?: ""
                            UpdateCabinetScreen(
                                id,
                                cabinetName,
                                cabinetDescription,
                                dateAddedCabinet,
                                isFavorite,
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