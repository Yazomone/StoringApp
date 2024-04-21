package com.plcoding.storingapp.Cabinets

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.plcoding.storingapp.data.Cabinet

data class CabinetState(
    val cabinets: List<Cabinet> = emptyList(),
    val cabinetName: MutableState<String> = mutableStateOf(""),
    val cabinetDescription: MutableState<String> = mutableStateOf(""),
    val searchCabinet: List<Cabinet> = emptyList(),
    val isFavorite: MutableState<Boolean> = mutableStateOf(false)
)
