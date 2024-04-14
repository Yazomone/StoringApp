package com.plcoding.storingapp.Cabinets

import android.util.Log
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
import com.plcoding.storingapp.Notes.NotesEvent

@Composable
fun UpdateCabinetScreen(
    id:String,
    cabinetName:String,
    cabinetDescription:String,
    dateAddedCabinet:String,
    navController: NavController,
    onEvent: (CabinetEvent) -> Unit
){
    var cabinetNameEmpty by remember { mutableStateOf(false) }
    var updatedcabinetName by remember { mutableStateOf(cabinetName) }
    var updatedcabinetDescription by remember { mutableStateOf(cabinetDescription) }
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
                if (updatedcabinetName.isEmpty()) {
                    cabinetNameEmpty = true
                } else {
                    if (updatedcabinetDescription.isEmpty()){
                        updatedcabinetDescription = "無敘述"
                    }
                    Log.d("id",id)
                    Log.d("updatedcabinetName",updatedcabinetName)
                    Log.d("updatedcabinetDescription",updatedcabinetDescription)
                    Log.d("dateAddedCabinet",dateAddedCabinet)
                    onEvent(
                        CabinetEvent.UpdateCabinet(
                            id.toInt(),
                            updatedcabinetName,
                            updatedcabinetDescription,
                            dateAddedCabinet.toLong()
                        )
                    )
                    navController.popBackStack()
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Update Note",
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
                value = updatedcabinetName,
                onValueChange = {
                    updatedcabinetName = it
                    if (it.isNotEmpty()) {
                        cabinetNameEmpty = false
                    }
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                ),
                placeholder = {
                    Text(text = "Cabinet Name")
                },
                isError = cabinetNameEmpty,
            )

            if (cabinetNameEmpty) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp),
                    text = "櫃子名稱不能為空白",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = updatedcabinetDescription,
                onValueChange = {
                    updatedcabinetDescription = it
                },
                placeholder = {
                    Text(text = "Description")
                }

            )
        }
    }
}