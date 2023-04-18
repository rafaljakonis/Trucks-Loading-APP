package com.example.trucksload.data

import com.google.gson.annotations.SerializedName

data class PutAwayResponse(
    @SerializedName("results")
    val result: PutAwaySpiltOrders
)