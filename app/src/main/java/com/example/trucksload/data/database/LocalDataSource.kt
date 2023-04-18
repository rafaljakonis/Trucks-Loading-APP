package com.example.trucksload.data.database

import com.example.trucksload.data.TruckLoadDao
import com.example.trucksload.data.TruckLoadEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val truckLoadDao: TruckLoadDao
) {

    fun readUserProperty(): Flow<TruckLoadEntity> {
        return truckLoadDao.readUserProperty()
    }
}