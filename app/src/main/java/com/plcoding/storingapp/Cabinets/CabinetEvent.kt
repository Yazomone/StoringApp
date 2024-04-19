package com.plcoding.storingapp.Cabinets

import androidx.compose.runtime.MutableState
import com.plcoding.storingapp.data.Cabinet

sealed interface CabinetEvent {

    data object SortCabinets: CabinetEvent

    data class DeleteCabinet(
        val cabinet: Cabinet
    ):CabinetEvent

    data class SaveCabinet(
        val cabinetName: String,
        val cabinetDescription: String
    ):CabinetEvent

    data class SearchCabinet(
        val query: String
    ):CabinetEvent

    data class UpdateCabinet(
        val id: Int,
        val updatedCabinetName: String,
        val updatedCabinetDescription: String,
        val dateAddedCabinet: Long
    ):CabinetEvent

    data class FavoriteCabinet(
        val id: Int,
        val isFavorite: MutableState<Boolean>
    ):CabinetEvent
}