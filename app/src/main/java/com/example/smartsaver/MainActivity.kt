package com.example.smartsaver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize buttons using findViewById
        val btnIncomeDetails = findViewById<Button>(R.id.btnIncomeDetails)
        val btnExpenseDetails = findViewById<Button>(R.id.btnExpenseDetails)
        val btnSavingsOverview = findViewById<Button>(R.id.btnSavingsOverview)
        val context: Context = this  // 'this' should be a Context instance
        if (CategoryManager.getCategories(context).isEmpty()) {
            val defaultCategories = listOf(
                Category(1, "Food"),
                Category(2, "Transport"),
                Category(3, "Utilities")
            )
            CategoryManager.saveCategories(context, defaultCategories)
        }


        // Set up click listeners for each button
        btnIncomeDetails.setOnClickListener {
            // Navigate to IncomeDetails
            val intent = Intent(this, IncomeDetails::class.java)
            startActivity(intent)
        }

        btnExpenseDetails.setOnClickListener {
            // Navigate to ExpenseDetails
            val intent = Intent(this, ExpenseDetails::class.java)
            startActivity(intent)
        }

        btnSavingsOverview.setOnClickListener {
            // Navigate to SavingsOverview
            val intent = Intent(this, SavingsOverview::class.java)
            startActivity(intent)
        }
    }
}
