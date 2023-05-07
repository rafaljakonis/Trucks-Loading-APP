package com.example.trucksload.data.network

import com.example.trucksload.data.model.Task
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import com.example.trucksload.data.network.model.BooleanResult
import com.example.trucksload.data.network.model.AssignOrderToUser
import com.example.trucksload.data.network.model.OrderActionRequest
import com.example.trucksload.data.network.model.ScannedElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface TruckLoadApi {
    @POST("/mobile-application-authorization/sign-in")
    suspend fun authenticateUser(
        @Body userToAuthenticate: UserToAuthenticate
    ): Response<User>

    @GET("/truck-load/get-active-orders")
    suspend fun getOrders(
    ): Response<ArrayList<Task>>

    @GET("/truck-load/get-operated-order")
    suspend fun getOperatedOrders(
        @Query("user-id") userId: Int
    ): Response<ArrayList<Task>>

    @POST("/truck-load/assign-order-to-user")
    suspend fun assignOrderToUser(
        @Body assignOrderToUser: AssignOrderToUser
    ): Response<BooleanResult>

    @POST("/truck-load/cancel-order")
    suspend fun cancelOrder(
        @Body cancelOrder: OrderActionRequest
    ): Response<BooleanResult>

    @POST("/truck-load/finish-order")
    suspend fun finishOrder(
        @Body finishOrder: OrderActionRequest
    ): Response<BooleanResult>

    @POST("/truck-load/set-element-scanned")
    suspend fun setElementScanned(
        @Body scannedElement: ScannedElement
    ): Response<BooleanResult>

    @Multipart
    @POST("/truck-load/upload-photo")
    suspend fun uploadPhoto(
        @Part("order-id") orderId: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<BooleanResult>
}