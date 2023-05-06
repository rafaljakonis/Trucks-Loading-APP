package com.example.trucksload.data.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userID: Int?,
    @SerializedName("truck_load_status_id")
    val status: Int,
    @SerializedName("status_name")
    val statusName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("localization")
    val location: String,
    @SerializedName("create_date")
    val createDate: String,
    @SerializedName("elements")
    val elements: List<Element>
)
