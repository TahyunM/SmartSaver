package com.example.smartsaver.repository

import com.example.smartsaver.model.Payment

class PaymentRepository {
    fun getPayments(): List<Payment> {
        return listOf(
            Payment("Rent", 1200.00, "28/04/2024"),
            Payment("Electricity Bill", 300.00, "05/05/2024"),
            Payment("Internet Subscription", 60.00, "15/05/2024"),
            Payment("Gym Membership", 40.00, "22/05/2024")
        )
    }
}
