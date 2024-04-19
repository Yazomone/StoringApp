package com.plcoding.storingapp.Notes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UpdateDataScreen (
    state: NotesState,
    id: String,
    title: String,
    description: String,
    dateAdded: String,
    cabinetId:String,
    noteAmount:String,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit
){
    var titleEmpty by remember { mutableStateOf(false) }
    var updatedTitle by remember { mutableStateOf(title) }
    var updatedDescription by remember { mutableStateOf(description) }
    Scaffold (
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

            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (updatedTitle.isEmpty()) {
                    titleEmpty = true
                } else {
                    if (updatedDescription.isEmpty()) {
                        updatedDescription = "無敘述"
                    }
                    onEvent(
                        NotesEvent.UpdateNote(
                            id.toInt(),
                            updatedTitle,
                            updatedDescription,
                            dateAdded.toLong(),
                            cabinetId.toInt(),
                            state.nodeAmount.value
                        )
                    )
                    navController.navigate("NotesScreen/${cabinetId.toInt()}/${updatedTitle}")
                }
            }) {
                Icon(imageVector = Icons.Rounded.Check,
                    contentDescription = "Update Note"
                )
            }
        }
    ) {paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = updatedTitle,
                onValueChange = {
                    updatedTitle = it
                    if (it.isNotEmpty()) {
                        titleEmpty = false
                    }
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                ),
                placeholder = {
                    Text(text = "Name")
                },
                isError = titleEmpty,
            )
            if (titleEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp),
                    text = "標題不能為空白",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = updatedDescription ?: "",
                onValueChange = {
                    updatedDescription = it
                },
                placeholder = {
                    Text(text = "description")
                }
            )

            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LaunchedEffect(key1 = noteAmount) {
                    state.nodeAmount.value = noteAmount.toInt()
                }
                updateCounter(
                    updatedNoteAmount = state.nodeAmount
                )
            }
        }
    }
}
@Composable
fun updateCounter(updatedNoteAmount: MutableState<Int>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { if (updatedNoteAmount.value > 0) updatedNoteAmount.value-- },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("-")
        }
        TextField(
            modifier = Modifier
                .width(90.dp)
                .wrapContentWidth(unbounded = false)
                .padding(16.dp),
            value = updatedNoteAmount.value.toString(),
            onValueChange = {
                if (it.matches(Regex("\\d*"))){
                    updatedNoteAmount.value = it.toIntOrNull() ?: 1
                    updatedNoteAmount.value = if (updatedNoteAmount.value > 100) 100 else updatedNoteAmount.value
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center),
            shape = RoundedCornerShape(10.dp)
        )
        Button(
            onClick = { updatedNoteAmount.value++ },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("+")
        }
    }
}