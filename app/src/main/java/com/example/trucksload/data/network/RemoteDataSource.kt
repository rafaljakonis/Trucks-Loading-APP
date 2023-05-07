package com.example.trucksload.data.network

import com.example.trucksload.data.PutAwayResponse
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val truckLoadApi: TruckLoadApi
) {
    suspend fun authenticateUser(userToAuthenticate: UserToAuthenticate): Response<User> {
        return truckLoadApi.authenticateUser(userToAuthenticate)
    }
    suspend fun getOrders(): Response<PutAwayResponse> {
        return truckLoadApi.getOrders()
    }

}