package com.plcoding.storingapp.Notes

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.storingapp.data.Note
import com.plcoding.storingapp.data.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteDoa: NoteDao
) : ViewModel() {
    private val isSortedByDateAdded = MutableStateFlow(true)

    val displayText = MutableSharedFlow<String>()

    val isTitleDuplicate = MutableStateFlow(false)

    // 當檢查標題是否重複時，更新這個 MutableState 的值
    fun checkDuplicateTitle(title: String, cabinetId: Int){

        viewModelScope.launch {
            val existingNote = noteDoa.findDuplicateTitle(cabinetId, title).first()
            isTitleDuplicate.value = existingNote.isNotEmpty()
        }
    }

    private val currentCabinetId = MutableStateFlow(0)
    private fun setCabinetId(cabinetId: Int) {
        currentCabinetId.value = cabinetId
    }

    private var notes = combine(isSortedByDateAdded, currentCabinetId) { sort, cabinetId ->
        if (sort) {
            noteDoa.getNotesOrderedByDateAdded(cabinetId)
        } else {
            noteDoa.getNotesOrderedByTitle(cabinetId)
        }
    }.flatMapLatest { it }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _searchResults = MutableStateFlow<List<Note>>(emptyList())

    val _state = MutableStateFlow(NotesState())
    val state =
        combine(_state, isSortedByDateAdded, notes, _searchResults) { state, isSortedByDateAdded, notes, searchResults ->
            state.copy(
                notes = notes,
                searchResults = searchResults
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesState())


    fun updateDisplayText(text: String) {
        viewModelScope.launch {
            displayText.emit(text)
        }
        Log.d("displayText",text)
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteDoa.deleteNote(event.note)
                    _searchResults.value = noteDoa.searchNotes(_searchResults.value.joinToString(" ") { it.title })

                }
            }

            is NotesEvent.SaveNote -> {
                val note = Note(
                    title = state.value.title.value,
                    description = state.value.description.value,
                    dateAdded = System.currentTimeMillis(),
                    cabinetId = event.cabinetId,
                    nodeAmount = state.value.nodeAmount.value
                )

                viewModelScope.launch {
                    noteDoa.upsertNote(note)
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
                    _searchResults.value = noteDoa.searchNotes(query)
                }
            }

            is NotesEvent.UpdateNote -> {
                val note = Note(
                    id = event.id,
                    title = event.updatedTitle,
                    description = event.updatedDescription,
                    dateAdded = event.dateAdded,
                    cabinetId = event.cabinetId,
                    nodeAmount = state.value.nodeAmount.value
                )
                viewModelScope.launch {
                    noteDoa.updateNote(note)
                }

                _state.update {
                    it.copy(
                        title = mutableStateOf(""),
                        description = mutableStateOf("")
                    )
                }
            }

            is NotesEvent.SetCabinetId -> {
                setCabinetId(event.cabinetId)
            }

            NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }

        }
    }


}