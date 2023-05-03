package com.example.trucksload.data.model

data class Task(
    val id: Int,
    val userID: Int,
    val status: Int,
    val description: String,
    val location: String,
    val createDate: String,
    val isPhoto: Boolean,
    val elements: List<Element>
)
