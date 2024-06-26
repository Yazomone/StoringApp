package com.plcoding.storingapp.Cabinets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CabinetScreen(
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

                IconButton(onClick = {
                    navController.navigate("MainScreen")
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                }
                Text(
                    text = "櫃子總攬",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    navController.navigate("SearchScreenAtMS")
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
        Box(
            modifier = Modifier
            .background(Color(0xFFEBE2D9))
        ){
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
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }
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
    val isFavorite = rememberSaveable { mutableStateOf(false) }

    val expanded = remember { mutableStateOf(false) }
    val info = remember { mutableStateOf(false) }
    val createTime = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())

    val itemCount = viewModel.getItemCountForCabinet(state.cabinets[index].id).collectAsState(initial = 0)

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFFEFE))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Inventory2,
                    contentDescription = "Inventory",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primaryContainer
                )

                Spacer(modifier = Modifier.size(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (state.cabinets[index].isFavorite) {
                            Icon(
                                imageVector = Icons.Rounded.Bookmark,
                                contentDescription = "Favorite",
                                modifier = Modifier.size(25.dp),
                                tint = MaterialTheme.colorScheme.primaryContainer
                            )
                        }
                        Text(
                            text = state.cabinets[index].cabinetName,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )

                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "物品種類: ${itemCount.value}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }

                IconButton(onClick = { navController.navigate("NotesScreen/${state.cabinets[index].id}/${state.cabinets[index].cabinetName}/${state.cabinets[index].cabinetDescription}") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Launch,
                        contentDescription = "More options",
                        modifier = Modifier
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
                IconButton(onClick = { expanded.value = !expanded.value }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "More options",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
            if (info.value) {
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Text(
                        text = "創建日期:${createTime.format(state.cabinets[index].dateAddedCabinet)}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )

                    Text(
                        text = "櫃子物品種類:${ state.cabinets[index].cabinetDescription }",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
        }
    }
    if(expanded.value){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFFFEFE)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            IconButton(
                onClick = {
                    id.value = state.cabinets[index].id.toString()
                    cabinetName.value = state.cabinets[index].cabinetName
                    cabinetDescription.value = state.cabinets[index].cabinetDescription
                    dateAddedCabinet.value = state.cabinets[index].dateAddedCabinet.toString()
                    isFavorite.value = state.cabinets[index].isFavorite
                    navController.navigate("UpdateCabinetScreen/${id.value}/${cabinetName.value}/${cabinetDescription.value}/${dateAddedCabinet.value}/${isFavorite.value.toString()}")
                },Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Update cabinet",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }

            IconButton(
                onClick = {
                    isFavorite.value = !state.cabinets[index].isFavorite
                    onEvent(CabinetEvent.FavoriteCabinet(state.cabinets[index].id,isFavorite))
                    expanded.value = false
                },Modifier.padding(8.dp)
            ) {

                Icon(
                    imageVector = if(state.cabinets[index].isFavorite)Icons.Rounded.Star else Icons.Rounded.StarBorder,
                    contentDescription = "like Note",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }

            DeleteButtonWithConfirmationDialog(onDeleteConfirmed = {
                onEvent(CabinetEvent.DeleteCabinet(state.cabinets[index]))
            },cabinetName=state.cabinets[index].cabinetName
            )

            IconButton(
                onClick = {
                    info.value = !info.value

                },Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = "Info",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }

        }
    }
}


@Composable
fun DeleteButtonWithConfirmationDialog(onDeleteConfirmed: () -> Unit,cabinetName:String) {
    val openDialog = remember { mutableStateOf(false) }
    val openSecondDialog = remember { mutableStateOf(false) }

    IconButton(onClick = { openDialog.value = true },Modifier.padding(8.dp)) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = "Delete Note",
            modifier = Modifier.size(35.dp),
            tint = MaterialTheme.colorScheme.primaryContainer
        )
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("注意!") },
            text = { Text("您確定要刪除${cabinetName}嗎？") },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        openSecondDialog.value = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("確認", color = MaterialTheme.colorScheme.onBackground)
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text("取消")
                }
            }
        )
    }
    if (openSecondDialog.value) {
        AlertDialog(
            onDismissRequest = { openSecondDialog.value = false },
            title = { Text("注意!!!") },
            text = { Text("刪除櫃子將失去所有物品喔") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteConfirmed()
                        openSecondDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("確認", color = MaterialTheme.colorScheme.onBackground)
                }
            },
            dismissButton = {
                Button(onClick = { openSecondDialog.value = false }) {
                    Text("取消")
                }
            }
        )
    }
}
