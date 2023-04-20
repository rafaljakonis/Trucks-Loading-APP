package com.example.trucksload.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TruckLoadEntity::class],
    version = 1,
    exportSchema = false
)

abstract class TruckLoadDataBase: RoomDatabase() {
    abstract fun truckLoadDao(): TruckLoadDao
}