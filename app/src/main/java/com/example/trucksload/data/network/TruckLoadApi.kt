package com.example.trucksload.data.network

import com.example.trucksload.data.PutAwayResponse
import com.example.trucksload.data.Task
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TruckLoadApi {
    @GET("/zkz/put-away/get-planned-data")
    suspend fun getOrders(
    ): Response<PutAwayResponse>
}