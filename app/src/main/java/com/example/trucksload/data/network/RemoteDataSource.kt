package com.example.trucksload.data.network

import com.example.trucksload.data.PutAwayResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val truckLoadApi: TruckLoadApi
) {

    suspend fun getOrders(): Response<PutAwayResponse> {
        return truckLoadApi.getOrders()
    }
}