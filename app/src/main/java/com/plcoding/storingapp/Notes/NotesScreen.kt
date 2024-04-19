package com.plcoding.storingapp.Notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.plcoding.storingapp.R
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotesScreen(
    state: NotesState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit,
    cabinetId: String,
    cabinetName:String
) {
    onEvent(NotesEvent.SetCabinetId(cabinetId.toInt()))
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                }
                /*
                IconButton(onClick = { onEvent(NotesEvent.SortNotes) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Sort,
                        contentDescription = "Sort Notes",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                */
                Text(
                    text = cabinetName,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    navController.navigate("SearchScreen")
                }) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search note")
                }


            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.title.value = ""
                state.description.value = ""
                navController.navigate("AddNoteScreen/${cabinetId}")
            }) {
                
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add new note")
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(state.notes.size) { index ->
                    NoteItem(
                        state = state,
                        index = index,
                        cabinetId = cabinetId,
                        navController = navController,
                        onEvent = onEvent
                    )
                }
            }
            if (state.notes.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(5.dp),
                ) {
                    Image(
                        modifier = Modifier.size(150.dp),
                        painter = painterResource(id = R.drawable.sadpic),  // 將 your_image 替換為您的圖片資源
                        contentDescription = "描述"  // 提供一個描述來提高無障礙訪問性
                    )
                    Text(
                        text = "櫃子裡沒東西~~",
                        fontSize = 20.sp,
                        color = Color.Gray,

                    )
                }
            }
        }

        
    }
}

@Composable
fun NoteItem(
    state: NotesState,
    cabinetId: String,
    index: Int,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit
) {
    val id = rememberSaveable { mutableStateOf("") }
    val noteTitle = rememberSaveable { mutableStateOf("") }
    val noteDescription = rememberSaveable { mutableStateOf("") }
    val dateAdded = rememberSaveable { mutableStateOf("") }
    val noteAmount = rememberSaveable { mutableStateOf(0) }

    val info = remember { mutableStateOf(false) }
    val expanded = remember { mutableStateOf(false) }
    val createTime = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = state.notes[index].title,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "物品數量: ${state.notes[index].nodeAmount}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            if(info.value){
                Text(
                    text = state.notes[index].description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = "創建日期:${createTime.format(state.notes[index].dateAdded)}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

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

    if (expanded.value){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            IconButton(
                onClick = {
                    id.value = state.notes[index].id.toString()
                    noteTitle.value = state.notes[index].title
                    noteDescription.value = state.notes[index].description
                    dateAdded.value = state.notes[index].dateAdded.toString()
                    noteAmount.value = state.notes[index].nodeAmount
                    navController.navigate("UpdateDataScreen/${id.value}/${noteTitle.value}/${noteDescription.value}/${dateAdded.value}/${cabinetId}/${noteAmount.value}")
                },Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Update Note",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            DeleteButtonWithConfirmationDialog(onDeleteConfirmed = {
                onEvent(NotesEvent.DeleteNote(state.notes[index]))
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
            text = { Text("您確定要刪除這個筆記嗎？") },
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
