package com.plcoding.storingapp.Main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.plcoding.storingapp.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController
){
    Scaffold(

    ) {Padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Padding)
                .background(Color(0xFFEBE2D9)),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
            ) {
                item {
                    Box(modifier = Modifier

                        .background(Color(0xFF424241))
                    ){
                        val pagerState = rememberPagerState(pageCount = { 3 })
                        HorizontalPager(state = pagerState) { page ->
                            when (page) {
                                0 -> Image(
                                    painter = painterResource(id = R.drawable.slideshow1), // 替換成您的圖片資源
                                    contentDescription = "描述",
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                        .height(175.dp)
                                )
                                1 -> Image(
                                    painter = painterResource(id = R.drawable.slideshow2), // 替換成您的圖片資源
                                    contentDescription = "描述",
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                        .height(175.dp)
                                )
                                2 -> Image(
                                    painter = painterResource(id = R.drawable.slideshow3), // 替換成您的圖片資源
                                    contentDescription = "描述",
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                        .height(175.dp)
                                )
                            }
                        }

                        LaunchedEffect(Unit) {
                            while (true) {
                                delay(5000)
                                if (pagerState.currentPage == pagerState.pageCount - 1) {
                                    pagerState.animateScrollToPage(0)
                                } else {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    }

                }


                item {
                    Row {
                        Button(
                            onClick = { navController.navigate("CabinetScreen") },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(0.6f)
                                .align(Alignment.CenterVertically)
                                .height(275.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = buttonColors(containerColor = Color(0xFFFDF4D8)),
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
                                    text = "櫃子\n\n總攬",
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
                                .height(275.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = buttonColors(containerColor = Color(0xFFFFFAEA)),
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

                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(0.5f)
                                .height(175.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = buttonColors(containerColor = Color(0xFFFFFAEA)),
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
                                .height(175.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = buttonColors(containerColor = Color(0xFFFFFAEA)),
                        ) {
                            Column(

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Book,
                                    contentDescription = "描述",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    text = "行動書籤",
                                    fontSize = 25.sp,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                )
                            }

                        }

                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(
                            onClick = { /* 處理按鈕點擊事件 */ },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(0.5f)
                                .height(175.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = buttonColors(containerColor = Color(0xFFFFFAEA)),
                        ) {
                            Column(

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "描述",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    text = "提醒",
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
                                .height(175.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = buttonColors(containerColor = Color(0xFFFFFAEA)),
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
}
