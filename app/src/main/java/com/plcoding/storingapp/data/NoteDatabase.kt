package com.plcoding.storingapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class, Cabinet::class],
    version = 2
)
abstract class NotesDatabase: RoomDatabase() {
    abstract val Notedao:NoteDao
    abstract val CabinetDao: CabinetDao
}