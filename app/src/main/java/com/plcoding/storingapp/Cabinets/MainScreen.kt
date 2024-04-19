package com.plcoding.storingapp.Cabinets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MainScreen(
    state: CabinetState,
    navController: NavController,
    onEvent:(CabinetEvent) -> Unit,
    viewModel: CabinetViewModel
){
    Scaffold(
        topBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                IconButton(onClick = { onEvent(CabinetEvent.SortCabinets) }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(Modifier.weight(1f))

                IconButton(onClick = {
                    navController.navigate("SearchScreen")
                }) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search note")
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.cabinetName.value = ""
                state.cabinetDescription.value = ""
                navController.navigate("AddCabinetScreen")
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add new note")
            }
        }
    ) {paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(state.cabinets.size) { index ->
                CabinetItem(
                    state = state,
                    index = index,
                    navController = navController,
                    onEvent = onEvent,
                    viewModel= viewModel
                )
            }
        }
    }
}


@Composable
fun CabinetItem(
    state: CabinetState,
    index: Int,
    navController: NavController,
    onEvent: (CabinetEvent) -> Unit,
    viewModel: CabinetViewModel
){
    val id = rememberSaveable { mutableStateOf("") }
    val cabinetName = rememberSaveable { mutableStateOf("") }
    val cabinetDescription = rememberSaveable { mutableStateOf("") }
    val dateAddedCabinet = rememberSaveable { mutableStateOf("") }

    val expanded = remember { mutableStateOf(false) }
    val info = remember { mutableStateOf(false) }
    val createTime = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    val isFavorite = remember { mutableStateOf(false) }

    val itemCount = viewModel.getItemCountForCabinet(state.cabinets[index].id).collectAsState(initial = 0)

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = state.cabinets[index].cabinetName,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = state.cabinets[index].cabinetDescription,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            if(info.value){
                Text(
                    text = "物品種類: ${itemCount.value}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "創建日期:${createTime.format(state.cabinets[index].dateAddedCabinet)}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

        }
        IconButton(onClick = {navController.navigate("NotesScreen/${state.cabinets[index].id}/${state.cabinets[index].cabinetName}")}) {
            Icon(
                imageVector = Icons.Rounded.Inventory2,
                contentDescription = "More options",
                modifier = Modifier.size(35.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        IconButton(onClick = { expanded.value = !expanded.value }) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "More options",
                modifier = Modifier.size(35.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
    if(expanded.value){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            IconButton(
                onClick = {
                    id.value = state.cabinets[index].id.toString()
                    cabinetName.value = state.cabinets[index].cabinetName
                    cabinetDescription.value = state.cabinets[index].cabinetDescription
                    dateAddedCabinet.value = state.cabinets[index].dateAddedCabinet.toString()
                    navController.navigate("UpdateCabinetScreen/${id.value}/${cabinetName.value}/${cabinetDescription.value}/${dateAddedCabinet.value}")
                },Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Update cabinet",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            IconButton(
                onClick = {
                    isFavorite.value = !isFavorite.value
                    onEvent(CabinetEvent.FavoriteCabinet(state.cabinets[index].id,isFavorite))

                },Modifier.padding(8.dp)
            ) {

                Icon(
                    imageVector = if(state.cabinets[index].isFavorite)Icons.Rounded.Star else Icons.Rounded.StarBorder,
                    contentDescription = "like Note",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }//有點問題

            DeleteButtonWithConfirmationDialog(onDeleteConfirmed = {
                onEvent(CabinetEvent.DeleteCabinet(state.cabinets[index]))
            })

            IconButton(
                onClick = {
                    info.value = !info.value

                },Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = "Info",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

        }
    }
}


@Composable
fun DeleteButtonWithConfirmationDialog(onDeleteConfirmed: () -> Unit) {
    val openDialog = remember { mutableStateOf(false) }

    IconButton(onClick = { openDialog.value = true },Modifier.padding(8.dp)) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = "Delete Note",
            modifier = Modifier.size(35.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("確認刪除") },
            text = { Text("您確定要刪除這個櫃子嗎？") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteConfirmed()
                        openDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("確認", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text("取消")
                }
            }
        )
    }
}
