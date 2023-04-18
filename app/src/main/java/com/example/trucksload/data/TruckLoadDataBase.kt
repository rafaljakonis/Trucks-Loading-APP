package com.example.trucksload.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TruckLoadEntity::class],
    version = 1,
    exportSchema = false
)

abstract class TruckLoadDataBase: RoomDatabase() {
    abstract fun truckLoadDao(): TruckLoadDao
}