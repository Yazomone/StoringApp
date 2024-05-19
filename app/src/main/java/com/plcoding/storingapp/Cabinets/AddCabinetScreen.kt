package com.plcoding.storingapp.Cabinets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCabinetScreen(
    state: CabinetState,
    navController: NavController,
    onEvent: (CabinetEvent) -> Unit
){
    var cabinetNameEmpty by remember { mutableStateOf(false) }
    val cabinetTypes = listOf("食物", "書籍", "衣著", "文具", "其他") // Add your cabinet types here
    var selectedCabinetType by remember { mutableStateOf(cabinetTypes[0]) }
    var expanded by remember { mutableStateOf(false) }

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
                    text = "新增櫃子",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (state.cabinetName.value.isEmpty()) {
                    cabinetNameEmpty = true
                } else {
                    if (state.cabinetDescription.value.isEmpty()){
                        state.cabinetDescription.value = "無敘述"
                    }
                    onEvent(
                        CabinetEvent.SaveCabinet(
                            cabinetName = state.cabinetName.value,
                            cabinetDescription = state.cabinetDescription.value
                        ))
                    navController.popBackStack()
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save Note",
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
                    value = state.cabinetName.value,
                    onValueChange = {
                        state.cabinetName.value = it
                        if (it.isNotEmpty()) {
                            cabinetNameEmpty = false
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 17.sp,
                        color = Color(0xFF383838)
                    ),
                    placeholder = {
                        Text(text = "櫃子名稱")
                    },
                    isError = cabinetNameEmpty,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFFFFFFF), // 背景顏色
                        disabledTextColor = Color.Gray, // 禁用狀態下的文字顏色
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary, // 聚焦時下劃線顏色
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant, // 未聚焦時下劃線顏色
                        disabledIndicatorColor = Color.Transparent, // 禁用狀態下下劃線顏色
                        errorIndicatorColor = MaterialTheme.colorScheme.error, // 錯誤狀態下下劃線顏色
                    )
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

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { expanded = true },
                        value = state.cabinetDescription.value,
                        onValueChange = {
                            state.cabinetDescription.value = it
                        },
                        readOnly = true,
                        textStyle = TextStyle(
                            fontSize = 17.sp,
                            color = Color(0xFF383838)
                        ),
                        trailingIcon = {
                            IconButton(onClick = {  expanded = !expanded  }) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Clear text")
                            }
                        },
                        placeholder = {
                            Text(text = "櫃子物品種類")
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
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        cabinetTypes.forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = type,
                                        fontSize = 17.sp,
                                    )},
                                onClick = {
                                    selectedCabinetType = type
                                    state.cabinetDescription.value = type
                                    expanded = false
                                })
                        }
                    }
                }
            }
        }

    }
}