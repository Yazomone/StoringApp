package com.plcoding.storingapp.Notes


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.plcoding.storingapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDataScreen (
    state: NotesState,
    id: String,
    title: String,
    description: String,
    dateAdded: String,
    cabinetId:String,
    noteAmount:String,
    cabinetName:String,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit,
    viewModel: NotesViewModel
){
    var titleEmpty by remember { mutableStateOf(false) }
    val isTitleDuplicate by viewModel.isTitleDuplicate.collectAsState()
    var DuplicateTitle by remember { mutableStateOf(false) }
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
                Text(
                    text = "物品修改",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {

                if (updatedTitle.isEmpty()) {
                    titleEmpty = true
                } else {
                    viewModel.viewModelScope.launch {
                        viewModel.checkDuplicateTitle(updatedTitle,cabinetId.toInt())
                        delay(100)
                        if (isTitleDuplicate && updatedTitle != title) {
                            DuplicateTitle = true
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
                            navController.navigate("NotesScreen/${cabinetId}/${cabinetName}")
                        }
                    }

                }
            }) {
                Icon(imageVector = Icons.Rounded.Check,
                    contentDescription = "Update Note"
                )
            }
        }
    ) {paddingValues ->
        Box(
            modifier = Modifier
                .background(Color(0xFFEBE2D9))
        ){
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
                        fontSize = 17.sp,
                        color = Color(0xFF383838)
                    ),
                    placeholder = {
                        Text(text = "物品名稱")
                    },
                    isError = titleEmpty,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFFFFFFF), // 背景顏色
                        disabledTextColor = Color.Gray, // 禁用狀態下的文字顏色
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary, // 聚焦時下劃線顏色
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant, // 未聚焦時下劃線顏色
                        disabledIndicatorColor = Color.Transparent, // 禁用狀態下下劃線顏色
                        errorIndicatorColor = MaterialTheme.colorScheme.error, // 錯誤狀態下下劃線顏色
                    )
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
                if (DuplicateTitle) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp),
                        text = "物品標題已重複",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = updatedDescription ?: "",
                    textStyle = TextStyle(
                        fontSize = 17.sp,
                        color = Color(0xFF383838)
                    ),
                    onValueChange = {
                        updatedDescription = it
                    },
                    placeholder = {
                        Text(text = "物品敘述")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFFFFFFF), // 背景顏色
                        disabledTextColor = Color.Gray, // 禁用狀態下的文字顏色
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary, // 聚焦時下劃線顏色
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant, // 未聚焦時下劃線顏色
                        disabledIndicatorColor = Color.Transparent, // 禁用狀態下下劃線顏色
                        errorIndicatorColor = MaterialTheme.colorScheme.error, // 錯誤狀態下下劃線顏色
                    )
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