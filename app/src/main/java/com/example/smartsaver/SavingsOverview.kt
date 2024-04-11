package com.example.smartsaver

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SavingsOverview : AppCompatActivity() {

    private val sharedPrefFile = "com.example.smartsaver.PREFERENCE_FILE_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_overview)

        val tvTotalSavings = findViewById<TextView>(R.id.tvTotalSavings)

        val sharedPref = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val totalIncome = sharedPref.getFloat("total_income", 0.0f)
        val totalExpenses = sharedPref.getFloat("total_expense", 0.0f)
        val savings = totalIncome - totalExpenses

        tvTotalSavings.text = "Total Savings: $savings"
    }
}
