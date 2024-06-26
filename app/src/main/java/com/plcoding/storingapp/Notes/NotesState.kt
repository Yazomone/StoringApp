package com.plcoding.storingapp.Notes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.plcoding.storingapp.data.Note

data class NotesState(
    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf(""),
    val searchResults: List<Note> = emptyList(),
    var nodeAmount:  MutableState<Int> = mutableStateOf(1),
    val expirationDate: MutableState<Long> = mutableStateOf(0)
)
