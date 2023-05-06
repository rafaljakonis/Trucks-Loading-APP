package com.example.trucksload.data.network.model

import com.google.gson.annotations.SerializedName

data class AssignOrderToUser(
    @SerializedName("user-id")
    val userId: Int,
    @SerializedName("order-id")
    val orderId: Int,
)
