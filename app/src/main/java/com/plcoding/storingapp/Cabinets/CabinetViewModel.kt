package com.plcoding.storingapp.Cabinets

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.storingapp.data.CabinetDao
import com.plcoding.storingapp.data.Cabinet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CabinetViewModel(
    private val cabinetdao: CabinetDao
): ViewModel() {
    private val isSortedByDateAdded = MutableStateFlow(true)

    private val sortTable = MutableStateFlow(false)

    private val favoriteCabinets = cabinetdao.getFavoriteCabinets()
    private val nonFavoriteCabinets = cabinetdao.getNonFavoriteCabinets()


    private var cabinets = sortTable.flatMapLatest { sort ->
        val sortedCabinets = favoriteCabinets.combine(nonFavoriteCabinets) { favoriteCabinets, nonFavoriteCabinets ->
            favoriteCabinets + nonFavoriteCabinets
        }
        if (!sort) {
            sortedCabinets
        } else {
            cabinetdao.getCabinetOrderedByDateAdded()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _searchCabinet = MutableStateFlow<List<Cabinet>>(emptyList())

    val _state = MutableStateFlow(CabinetState())
    val state =
        combine(_state, isSortedByDateAdded, cabinets, _searchCabinet) { state, isSortedByDateAdded, cabinets, searchCabinet ->
            state.copy(
                cabinets = cabinets,
                searchCabinet = searchCabinet
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CabinetState())

    fun getItemCountForCabinet(cabinetId: Int): Flow<Int> = flow {
        cabinetdao.getNoteCountByCabinetId(cabinetId).collect { count ->
            emit(count)
        }
    }


    fun onEvent(event: CabinetEvent) {
        when(event){
            is CabinetEvent.DeleteCabinet -> {
                viewModelScope.launch {
                    cabinetdao.deleteCabinet(event.cabinet)
                }
            }

            is CabinetEvent.SaveCabinet -> {
                val cabinet = Cabinet(
                    cabinetName = state.value.cabinetName.value,
                    cabinetDescription = state.value.cabinetDescription.value,
                    dateAddedCabinet = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    cabinetdao.upsertCabinet(cabinet)
                }

                _state.update {
                    it.copy(
                        cabinetName = mutableStateOf(""),
                        cabinetDescription = mutableStateOf("")
                    )
                }

            }

            is CabinetEvent.SearchCabinet -> {
                val query = event.query
                viewModelScope.launch {
                    _searchCabinet.value = cabinetdao.searchCabinet(query)
                }
            }

            is CabinetEvent.UpdateCabinet -> {
                val cabinet = Cabinet(
                    id = event.id,
                    cabinetName = event.updatedCabinetName,
                    cabinetDescription = event.updatedCabinetDescription,
                    dateAddedCabinet = event.dateAddedCabinet
                )
                viewModelScope.launch {
                    cabinetdao.updateCabinet(cabinet)
                }
                _state.update {
                    it.copy(
                        cabinetName = mutableStateOf(""),
                        cabinetDescription = mutableStateOf("")
                    )
                }
            }

            is CabinetEvent.FavoriteCabinet -> {
                viewModelScope.launch {
                    cabinetdao.updateFavoriteStatus(event.id,event.isFavorite.value)
                }
            }

            CabinetEvent.SortCabinets -> {
                sortTable.value = !sortTable.value
            }
        }
    }
}