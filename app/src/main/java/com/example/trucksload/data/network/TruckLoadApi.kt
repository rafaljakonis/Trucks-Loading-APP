package com.example.trucksload.data.network

import com.example.trucksload.data.model.Task
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import com.example.trucksload.data.network.model.BooleanResult
import com.example.trucksload.data.network.model.AssignOrderToUser
import com.example.trucksload.data.network.model.OrderActionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TruckLoadApi {
    @POST("/mobile-application-authorization/sign-in")
    suspend fun authenticateUser(
        @Body userToAuthenticate: UserToAuthenticate
    ): Response<User>

    @GET("/truck-load/get-active-orders")
    suspend fun getOrders(
    ): Response<ArrayList<Task>>

    @POST("/truck-load/assign-order-to-user")
    suspend fun assignOrderToUser(
        @Body assignOrderToUser: AssignOrderToUser
    ): Response<BooleanResult>

    @POST("/truck-load/cancel-order")
    suspend fun cancelOrder(
        @Body cancelOrder: OrderActionRequest
    ): Response<BooleanResult>
}