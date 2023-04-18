package com.example.trucksload.data

import kotlinx.parcelize.Parcelize

data class Task(
    val id: Int,
    val userID: Int,
    val description: String,
    val location: String,
    val createDate: String,
    val isPhoto: Boolean,
    val elements: List<Element>
)
