package com.plcoding.roomtest2.presentation

import androidx.room.Query
import com.plcoding.roomtest2.data.Note

sealed interface NotesEvent {

    data object SortNotes: NotesEvent

    data class DeleteNote(
        val note: Note
    ):NotesEvent

    data class SaveNote(
        val title: String,
        val description: String
    ):NotesEvent

    data class SearchNote(
        val query: String
    ):NotesEvent

    data class UpdateNote(
        val title: String,
        val description: String
    ):NotesEvent
}
