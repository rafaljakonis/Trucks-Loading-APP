package com.example.trucksload.data.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val truckLoadDao: TruckLoadDao
) {
    fun readUserProperty(): Flow<TruckLoadEntity> {
        return truckLoadDao.readUserProperty()
    }
}