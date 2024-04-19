package com.plcoding.storingapp.data

import androidx.compose.runtime.MutableState
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

    @Query("SELECT * FROM Cabinet WHERE isFavorite = 1 ORDER BY dateAddedCabinet DESC")
    fun getFavoriteCabinets(): Flow<List<Cabinet>>

    @Query("SELECT * FROM Cabinet WHERE isFavorite = 0 ORDER BY dateAddedCabinet DESC")
    fun getNonFavoriteCabinets(): Flow<List<Cabinet>>

    @Query("SELECT * FROM Cabinet ORDER BY isFavorite DESC, dateAddedCabinet DESC")
    fun sortWithFavoriteCabinetss(): Flow<List<Cabinet>>

    @Query("UPDATE Cabinet SET isFavorite = :isFavorite WHERE id = :cabinetId")
    suspend fun updateFavoriteStatus(cabinetId: Int,isFavorite:Boolean)

    @Query("SELECT * FROM Cabinet ORDER BY dateAddedCabinet")
    fun getCabinetOrderedByDateAdded(): Flow<List<Cabinet>>

    @Query("SELECT * FROM Cabinet ORDER BY cabinetName ASC")
    fun getCabinetOrderedByTitle(): Flow<List<Cabinet>>

    @Query("SELECT * FROM Cabinet WHERE cabinetName LIKE :query || '%' ")
    suspend fun searchCabinet(query: String): List<Cabinet>

    @Query("SELECT COUNT(*) FROM note WHERE cabinetId = :cabinetId")
    fun getNoteCountByCabinetId(cabinetId: Int): Flow<Int>

}