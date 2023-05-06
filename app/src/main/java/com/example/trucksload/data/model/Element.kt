package com.example.trucksload.data.model

import com.google.gson.annotations.SerializedName

data class Element(
    @SerializedName("element_id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("localization")
    val localization: String,
    @SerializedName("is_complete")
    val isComplete: Int
)
