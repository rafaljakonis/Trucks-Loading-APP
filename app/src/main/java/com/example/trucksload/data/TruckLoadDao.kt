package com.example.trucksload.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckLoadDao {

    @Query("SELECT * FROM user LIMIT 1")
    fun readUserProperty(): Flow<TruckLoadEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewUser(truckLoadEntity: TruckLoadEntity)

    @Delete
    suspend fun removeSavedUser(truckLoadEntity: TruckLoadEntity)
}