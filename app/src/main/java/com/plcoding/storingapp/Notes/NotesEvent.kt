package com.plcoding.storingapp.Notes

import com.plcoding.storingapp.data.Note

sealed interface NotesEvent {

    data object SortNotes: NotesEvent

    data class DeleteNote(
        val note: Note
    ):NotesEvent
    data class SetCabinetId(
        val cabinetId: Int
    ):NotesEvent

    data class SaveNote(
        val title: String,
        val description: String,
        val cabinetId: Int
    ):NotesEvent

    data class SearchNote(
        val query: String
    ):NotesEvent

    data class UpdateNote(
        val id:Int,
        val updatedTitle: String,
        val updatedDescription: String,
        val dateAdded: Long
    ):NotesEvent
}
