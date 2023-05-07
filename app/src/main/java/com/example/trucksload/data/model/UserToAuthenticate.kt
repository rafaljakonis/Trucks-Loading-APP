package com.example.trucksload.data.model

import com.google.gson.annotations.SerializedName

data class UserToAuthenticate(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("application_name")
    val applicationName: String,
    @SerializedName("application_version")
    val applicationVersion: String,
    @SerializedName("phone_uid")
    val phoneUid: String
)