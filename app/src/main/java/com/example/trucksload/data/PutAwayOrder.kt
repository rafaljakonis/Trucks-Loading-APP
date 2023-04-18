package com.example.trucksload.data

data class PutAwayOrder(
    val id: Int,
    val operator: String?,
    val task: String,
    val datetime: String?,
    val flagActive: Int,
    val task_date: String?,
    val important: Int,
    val deadLine: String?,
    val done: Int,
    val datetime_done: String?,
    val track_code: String,
    val gate: String?,
    val time: String?,
)