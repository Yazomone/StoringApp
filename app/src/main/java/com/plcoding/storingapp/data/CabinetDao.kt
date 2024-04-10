package com.plcoding.storingapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CabinetDao {
    @Upsert
    suspend fun upsertCabinet(cabinet: Cabinet)
    @Delete
    suspend fun deleteCabinet(cabinet: Cabinet)
    @Update
    suspend fun updateCabinet(cabinet: Cabinet)

    @Query("SELECT * FROM Cabinet ORDER BY dateAddedCabinet")
    fun getCabinetOrderedByDateAdded(): Flow<List<Cabinet>>

    @Query("SELECT * FROM Cabinet ORDER BY cabinetName ASC")
    fun getCabinetOrderedByTitle(): Flow<List<Cabinet>>

    @Query("SELECT * FROM Cabinet WHERE cabinetName LIKE :query || '%' ")
    suspend fun searchCabinet(query: String): List<Cabinet>
}