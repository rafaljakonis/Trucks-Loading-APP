package com.example.trucksload.data

import com.google.gson.annotations.SerializedName

data class PutAwaySpiltOrders(
    @SerializedName("urgent")
    val urgentList: List<PutAwayOrder>,
    @SerializedName("normal")
    val normalList: List<PutAwayOrder>
)