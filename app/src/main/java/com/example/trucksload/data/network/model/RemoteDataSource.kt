package com.example.trucksload.data.network.model

import com.example.trucksload.data.model.Task
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import com.example.trucksload.data.network.TruckLoadApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val truckLoadApi: TruckLoadApi
) {
    suspend fun authenticateUser(userToAuthenticate: UserToAuthenticate): Response<User> {
        return truckLoadApi.authenticateUser(userToAuthenticate)
    }

    suspend fun getOrders(): Response<ArrayList<Task>> {
        return truckLoadApi.getOrders()
    }

    suspend fun assignOrderToUser(assignOrderToUser: AssignOrderToUser): Response<BooleanResult> {
        return truckLoadApi.assignOrderToUser(assignOrderToUser)
    }

    suspend fun cancelOrder(cancelOrder: OrderActionRequest): Response<BooleanResult> {
        return truckLoadApi.cancelOrder(cancelOrder)
    }

}