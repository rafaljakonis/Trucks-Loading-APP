package com.example.trucksload.data

import kotlinx.parcelize.Parcelize

data class Task(
    val name: String,
    val description: String,
    val location: String,
    val elements: List<Element>
)
