package com.example.trucksload.data.network

import com.example.trucksload.data.PutAwayResponse
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TruckLoadApi {
    @POST("/mobile-application-authorization/sign-in")

    suspend fun authenticateUser(@Body userToAuthenticate: UserToAuthenticate
    ): Response<User>

    @GET("/zkz/put-away/get-planned-data")
    suspend fun getOrders(
    ): Response<PutAwayResponse>
}