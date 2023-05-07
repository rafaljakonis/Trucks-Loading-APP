package com.example.trucksload.data.network.model

import com.google.gson.annotations.SerializedName

data class ScannedElement(
    @SerializedName("order-id")
    val orderId: Int,
    @SerializedName("element-id")
    val elementId: Int,
    @SerializedName("element-code")
    val scannedCode: String
)
