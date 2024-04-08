package com.plcoding.storingapp.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.storingapp.data.Note
import com.plcoding.storingapp.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(private val dao: NoteDao) : ViewModel() {
    private val isSortedByDateAdded = MutableStateFlow(true)

    private var notes = isSortedByDateAdded.flatMapLatest { sort ->
            if (sort) {
                dao.getNotesOrderedByDateAdded()
            } else {
                dao.getNotesOrderedByTitle()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
    private val _searchResults = MutableStateFlow<List<Note>>(emptyList())

    val _state = MutableStateFlow(NotesState())
    val state =
        combine(_state, isSortedByDateAdded, notes, _searchResults) { state, isSortedByDateAdded, notes, searchResults ->
            state.copy(
                notes = notes,
                searchResults = searchResults
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesState())

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                    _searchResults.value = dao.searchNotes(_searchResults.value.map { it.title }.joinToString(" "))

                }
            }

            is NotesEvent.SaveNote -> {
                val note = Note(
                    title = state.value.title.value,
                    description = state.value.description.value,
                    dateAdded = System.currentTimeMillis()
                )

                viewModelScope.launch {
                    dao.upsertNote(note)
                }

                _state.update {
                    it.copy(
                        title = mutableStateOf(""),
                        description = mutableStateOf("")
                    )
                }
            }

            is NotesEvent.SearchNote -> {
                val query = event.query
                viewModelScope.launch {
                    _searchResults.value = dao.searchNotes(query)
                }
            }

            is NotesEvent.UpdateNote -> {
                val note = Note(
                    id = event.id,
                    title = event.updatedTitle,
                    description = event.updatedDescription,
                    dateAdded = event.dateAdded
                )
                viewModelScope.launch {
                    dao.updateNote(note)
                }

                _state.update {
                    it.copy(
                        title = mutableStateOf(""),
                        description = mutableStateOf("")
                    )
                }
            }

            NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }

        }
    }


}