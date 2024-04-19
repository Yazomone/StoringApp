package com.plcoding.storingapp.Cabinets

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.plcoding.storingapp.Notes.NotesEvent
import com.plcoding.storingapp.Notes.NotesState
import com.plcoding.storingapp.Notes.NotesViewModel
import com.plcoding.storingapp.data.Note

@Composable
fun SearchScreenAtMS (
    noteState: NotesState,
    cabinetState:CabinetState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit,
    viewModel: NotesViewModel
){
    val searchQuery = remember { mutableStateOf("") }

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
                    onEvent(NotesEvent.SearchCabinet(""))
                    searchQuery.value = ""
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                }

                TextField(
                    value = searchQuery.value,
                    onValueChange = { newValue ->
                        searchQuery.value = newValue
                        onEvent(NotesEvent.SearchCabinet(newValue))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            searchQuery.value = ""
                        }
                    ),
                    placeholder = {
                        Text(text = "Search by item name")
                    },
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

            items(noteState.searchResults.size) { index ->
                SearchItem(
                    note = noteState.searchResults[index],
                    viewModel = viewModel,
                    navController = navController,
                    onEvent = onEvent
                )
            }
        }
    }
}


@Composable
fun SearchItem(
    note: Note,
    viewModel: NotesViewModel,
    onEvent: (NotesEvent) -> Unit,
    navController:NavController
) {
    val cabinetName = viewModel.getCabinetName(note.cabinetId).collectAsState(initial = "")

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(top = 15.dp, bottom = 15.dp, start = 12.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Rounded.Inventory2,
            contentDescription = "Favorite",
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = cabinetName.value,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "(櫃子查詢結果如下)",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

        }

        IconButton(onClick = {
            navController.navigate("NotesScreen/${note.cabinetId}/${cabinetName.value}")
            onEvent(NotesEvent.SearchNote("",0))
        }) {
            Log.d("note.cabinetId",note.cabinetId.toString())
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Launch,
                contentDescription = "More options",
                modifier = Modifier.size(35.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.size(35.dp))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 15.dp, start = 12.dp, end = 12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),

        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Rounded.AttachFile,
            contentDescription = "Favorite",
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            modifier = Modifier
                .padding(12.dp)
                .weight(1f)
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
    }
}