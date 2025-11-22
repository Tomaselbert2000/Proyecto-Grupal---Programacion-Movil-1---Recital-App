package com.example.myapplication.data.superclass

import java.time.LocalDateTime

data class Transaction(
    val transactionId: Long,
    val amount: Double,
    val userId: Long,
    val dateAndTime: LocalDateTime = LocalDateTime.now()
) {
}