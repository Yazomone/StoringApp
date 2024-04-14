package com.plcoding.storingapp.Notes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UpdateDataScreen (
    id: String,
    title: String,
    description: String,
    dateAdded: String,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit
){
    var titleEmpty by remember { mutableStateOf(false) }
    var updatedTitle by remember { mutableStateOf("") }
    var updatedDescription by remember { mutableStateOf("") }
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
                            dateAdded.toLong()
                        )
                    )
                    navController.popBackStack()
                }
            }) {
                Icon(imageVector = Icons.Rounded.Check,
                    contentDescription = "Save Note"
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
                    Text(text = title)
                },
                isError = titleEmpty,
            )

            if (titleEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp),
                    text = "修改標題不能為空白",
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
                    Text(text = description)
                }

            )
        }

    }


}