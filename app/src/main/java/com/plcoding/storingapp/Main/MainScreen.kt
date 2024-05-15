package com.plcoding.storingapp.Main

import android.widget.HorizontalScrollView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.plcoding.storingapp.Cabinets.CabinetEvent
import com.plcoding.storingapp.Cabinets.CabinetItem
import com.plcoding.storingapp.Cabinets.CabinetState
import com.plcoding.storingapp.Cabinets.CabinetViewModel

@Composable
fun MainScreen(
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

//                IconButton(onClick = {
//
//                }) {
//                    Icon(
//                        imageVector = Icons.Rounded.Settings,
//                        contentDescription = "Settings",
//                        modifier = Modifier.size(35.dp),
//                        tint = MaterialTheme.colorScheme.onPrimary
//                    )
//                }

            }
        }
    ) {Padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = { navController.navigate("CabinetScreen") },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(0.6f)
                            .align(Alignment.CenterVertically)
                            .height(300.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(10.dp, MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(

                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = "描述",
                                modifier = Modifier
                                    .size(60.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "櫃子\n\n畫面",
                                fontSize = 30.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                    }

                    Button(
                        onClick = { /* 處理按鈕點擊事件 */ },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(1f)
                            .height(300.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(10.dp, MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(

                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "描述",
                                modifier = Modifier
                                    .size(60.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "歷史\n\n紀錄",
                                fontSize = 30.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                        }


                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(0.5f)
                            .height(150.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(10.dp, MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(

                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "描述",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(Modifier.height(5.dp))
                            Text(
                                text = "購物清單",
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                    }
                    Button(
                        onClick = { /* 處理按鈕點擊事件 */ },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(1f)
                            .height(150.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(10.dp, MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(

                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "描述",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(Modifier.height(5.dp))
                            Text(
                                text = "設定",
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                    }
                }
            }

        }

    }
}