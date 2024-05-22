package com.plcoding.storingapp.Notes

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen (
    state: NotesState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit,
    viewModel: NotesViewModel,
    cabinetId:String
){
    var titleEmpty by remember { mutableStateOf(false) }
    var DuplicateTitle by remember { mutableStateOf(false) }
    val isTitleDuplicate by viewModel.isTitleDuplicate.collectAsState()
    val scannedTitle by viewModel.displayText.collectAsState(initial = "")
    var title by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val selectedDate =  remember { mutableStateOf("") }

    title = scannedTitle

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
                    text = "新增物品",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                FloatingActionButton(onClick = {
                    navController.navigate("CameraPermission")
                }) {
                    Icon(imageVector = Icons.Rounded.PhotoCamera,
                        contentDescription = "Text Recognition"
                    )
                }

                FloatingActionButton(onClick = {


                    if (state.title.value.isEmpty()) {
                        titleEmpty = true
                    } else {
                        viewModel.viewModelScope.launch {
                            viewModel.checkDuplicateTitle(state.title.value,cabinetId.toInt())
                            delay(100)
                            if (isTitleDuplicate) {
                                DuplicateTitle = true
                            } else {
                                DuplicateTitle = false
                                if (state.description.value.isEmpty()){
                                    state.description.value = "無敘述"
                                }
                                onEvent(NotesEvent.SaveNote(
                                    title = state.title.value,
                                    description = state.description.value,
                                    cabinetId = cabinetId.toInt(),
                                    nodeAmount = state.nodeAmount.value
                                ))
                                navController.popBackStack()
                            }
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Rounded.Check,
                        contentDescription = "Save Note"
                    )
                }

            }
        },

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
                    value = title,

                    onValueChange = {
                        title = it
                        state.title.value = title
                        if (title.isNotEmpty()) {
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
                        containerColor = Color(0xFFFFFFFF)
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



                if (openDialog.value) {
                    val context = LocalContext.current
                    val c = Calendar.getInstance()
                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)

                    DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
                        val selectedDateString = "${year}/${monthOfYear + 1}/${dayOfMonth}"
                        selectedDate.value = selectedDateString
                        openDialog.value = false
                        val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                        val date = format.parse(selectedDateString)
                        state.expirationDate.value = date?.time ?: 0L
                    }, year, month, day).show()
                }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = selectedDate.value,
                    onValueChange = { },
                    readOnly = true,
                    textStyle = TextStyle(
                        fontSize = 17.sp,
                        color = Color(0xFF383838)
                    ),
                    placeholder = {
                        Text(text = "物品有效期限(選填)")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFFFFFFF)
                    ),
                    trailingIcon = {
                        IconButton(onClick = {  openDialog.value = !openDialog.value  }) {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Clear text")
                        }
                    }
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = state.description.value,
                    onValueChange = {
                        state.description.value = it
                    },
                    textStyle = TextStyle(
                        fontSize = 17.sp,
                        color = Color(0xFF383838)
                    ),
                    placeholder = {
                        Text(text = "物品敘述")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFFFFFFF)
                    )
                )

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Counter(
                        nodeAmount = state.nodeAmount
                    )
                }

            }
        }

    }
}

@Composable
fun Counter( nodeAmount : MutableState<Int>) {
    var nodeCount by remember { mutableStateOf(1) }
    nodeAmount.value = nodeCount
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Button(
            onClick = { if (nodeCount > 1) nodeCount -- },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("-")
        }
        TextField(
            modifier = Modifier
                .width(90.dp)
                .wrapContentWidth(unbounded = false)
                .padding(16.dp),
            value = nodeCount.toString(),
            onValueChange = {
                if (it.matches(Regex("\\d*"))){
                    nodeAmount.value = it.toIntOrNull() ?: 1
                    nodeAmount.value = if (nodeCount > 100) 100 else nodeCount
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
            onClick = { nodeCount ++ },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("+")
        }
    }
}