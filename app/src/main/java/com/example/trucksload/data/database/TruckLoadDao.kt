package com.example.trucksload.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TruckLoadDao {

    @Query("SELECT * FROM user LIMIT 1")
    fun readUserProperty(): Flow<TruckLoadEntity>
    @Transaction
    suspend fun insertNewUser(truckLoadEntity: TruckLoadEntity) {
        removeSavedUser()
        insertUser(truckLoadEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(truckLoadEntity: TruckLoadEntity)
    
    @Query("DELETE FROM user")
    suspend fun removeSavedUser()
}