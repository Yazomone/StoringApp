package com.plcoding.storingapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)
    @Delete
    suspend fun deleteNote(note: Note)
    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM note WHERE cabinetId = :cabinetId ORDER BY dateAdded")
    fun getNotesOrderedByDateAdded(cabinetId: Int): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE cabinetId = :cabinetId ORDER BY title ASC")
    fun getNotesOrderedByTitle(cabinetId: Int): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE cabinetId = :cabinetId and :query != '' AND title LIKE :query || '%' ")
    suspend fun searchNotes(query: String,cabinetId: Int): List<Note>

    @Query("SELECT * FROM note WHERE :query != '' AND title LIKE :query || '%' ")
    suspend fun searchNotes(query: String): List<Note>

    @Query("SELECT * FROM note WHERE cabinetId = :cabinetId and title like :title ORDER BY title ASC")
    fun findDuplicateTitle(cabinetId: Int,title: String): Flow<List<Note>>

    @Query("SELECT cabinetName FROM Cabinet WHERE id = :cabinetId")
    fun getCabinetById(cabinetId: Int): Flow<String>
}