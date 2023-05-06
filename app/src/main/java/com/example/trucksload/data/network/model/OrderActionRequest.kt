package com.example.trucksload.data.network.model

import com.google.gson.annotations.SerializedName

data class OrderActionRequest(
    @SerializedName("order-id")
    val orderId: Int
)
