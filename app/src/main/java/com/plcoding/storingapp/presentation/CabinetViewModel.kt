package com.plcoding.storingapp.presentation

import androidx.lifecycle.ViewModel
import com.plcoding.storingapp.data.CabinetDao

class CabinetViewModel(
    private val cabinetdao: CabinetDao
): ViewModel() {
    fun onEvent(event: NotesEvent) {

    }
}