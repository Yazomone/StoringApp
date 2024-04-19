package com.plcoding.storingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cabinet(
    val cabinetName: String,
    val cabinetDescription: String,
    val dateAddedCabinet: Long,
    val isFavorite: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

)


