package com.example.smartsaver.model

data class Payment(
    val description: String,
    val amount: Double,
    val dueDate: String  // Stored as "dd/MM/yyyy"
)
