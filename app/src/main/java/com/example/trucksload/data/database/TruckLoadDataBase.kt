package com.example.trucksload.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TruckLoadEntity::class],
    version = 2,
    exportSchema = false
)

abstract class TruckLoadDataBase: RoomDatabase() {
    abstract fun truckLoadDao(): TruckLoadDao
}