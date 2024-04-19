package com.plcoding.storingapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Cabinet::class,
    parentColumns = ["id"],
    childColumns = ["cabinetId"],
    onDelete = ForeignKey.CASCADE
)])
data class Note(
    val title: String,
    val description: String,
    val dateAdded: Long,
    val cabinetId: Int,
    val nodeAmount: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
