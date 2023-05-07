package com.example.trucksload.data.network.model

import com.example.trucksload.data.model.Task
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import com.example.trucksload.data.network.TruckLoadApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Part
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

    suspend fun finishOrder(finishOrder: OrderActionRequest): Response<BooleanResult> {
        return truckLoadApi.finishOrder(finishOrder)
    }

    suspend fun setElementScanned(scannedElement: ScannedElement): Response<BooleanResult> {
        return truckLoadApi.setElementScanned(scannedElement)
    }

    suspend fun uploadPhoto(orderId: RequestBody,file: MultipartBody.Part): Response<BooleanResult> {
        return truckLoadApi.uploadPhoto(orderId, file)
    }

    suspend fun getOperatedOrder(userId: Int): Response<ArrayList<Task>> {
        return truckLoadApi.getOperatedOrders(userId)
    }

}