package com.plcoding.storingapp.Notes

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.plcoding.storingapp.R
import com.plcoding.storingapp.data.Note
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun SearchScreen (
    state: NotesState,
    cabinetId: String,
    cabinetName:String,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit
){

    val searchQuery = remember { mutableStateOf("")}
    val showEmptySearch = remember { mutableStateOf(false) }
    LaunchedEffect(searchQuery.value) {
        showEmptySearch.value = false
        delay(100)
        if (searchQuery.value.isNotEmpty()) {
            showEmptySearch.value = true
        }
    }

    Scaffold (
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
                    navController.popBackStack()
                    onEvent(NotesEvent.SearchNote("",0))
                    searchQuery.value = ""
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                }

                TextField(
                    value = searchQuery.value,
                    onValueChange = { newValue ->
                        searchQuery.value = newValue
                        onEvent(NotesEvent.SearchNote(newValue,cabinetId.toInt()))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    shape = RoundedCornerShape(10.dp)
                )


            }
        }
    ){ paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(state.searchResults.size) { index ->
                SearchItem(
                    state = state,
                    note = state.searchResults[index],
                    onEvent = onEvent,
                    cabinetName = cabinetName,
                    navController = navController,
                    searchQuery = searchQuery
                )
            }
        }
        if (showEmptySearch.value && state.searchResults.isEmpty()) {
            Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(5.dp),
                ) {
                    Image(
                        modifier = Modifier.size(200.dp),
                        painter = painterResource(id = R.drawable.confuseface),
                        contentDescription = "描述"
                    )
                    Text(
                        text = "咦?這裡怎麼沒東西???",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}


@Composable
fun SearchItem(
    state: NotesState,
    note: Note,
    cabinetName:String,
    navController: NavController,
    searchQuery: MutableState<String>,
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
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Rounded.AttachFile,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = note.title,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = note.description,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
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


            if(info.value){
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Text(
                        text = "物品數量: ${note.nodeAmount}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Text(
                        text = "創建日期:${createTime.format(note.dateAdded)}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
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
                    id.value = note.id.toString()
                    noteTitle.value = note.title
                    noteDescription.value = note.description
                    dateAdded.value = note.dateAdded.toString()
                    noteAmount.value = note.nodeAmount
                    navController.navigate("UpdateDataScreen/${id.value}/${noteTitle.value}/${noteDescription.value}/${dateAdded.value}/${note.cabinetId}/${noteAmount.value}/${cabinetName}")
                },Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Update Note",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            DeleteButtonWithConfirmationDialoginSearchScreen(onDeleteConfirmed = {
                onEvent(NotesEvent.DeleteNote(note))
                onEvent(NotesEvent.SearchNote(searchQuery.value,note.cabinetId))
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
fun DeleteButtonWithConfirmationDialoginSearchScreen(onDeleteConfirmed: () -> Unit) {
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
            title = { Text("注意!") },
            text = { Text("您確定要刪除這個物品嗎？") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteConfirmed()
                        openDialog.value = false
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
}
