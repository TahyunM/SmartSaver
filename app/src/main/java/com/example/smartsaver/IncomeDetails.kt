package com.example.smartsaver

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// This class represents the income details of the user
class IncomeDetails : AppCompatActivity() {

    // UI elements
    private lateinit var etIncome: EditText
    private lateinit var btnSaveIncome: Button
    private lateinit var etSavingsGoal: EditText
    private lateinit var btnSaveGoal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_details)

        // Initialize UI elements
        etIncome = findViewById(R.id.etIncome)
        btnSaveIncome = findViewById(R.id.btnSaveIncome)
        etSavingsGoal = findViewById(R.id.etSavingsGoal)
        btnSaveGoal = findViewById(R.id.btnSaveGoal)

        // Set click listeners for the buttons
        btnSaveIncome.setOnClickListener { saveIncome() }
        btnSaveGoal.setOnClickListener { saveGoal() }
    }

    // Method to save the income
    private fun saveIncome() {
        val incomeString = etIncome.text.toString().trim()

        // Check if the income string is empty
        if (incomeString.isEmpty()) {
            Toast.makeText(this, "Please enter your income.", Toast.LENGTH_SHORT).show()
            return
        }

        // Convert the income string to a double
        val income = incomeString.toDoubleOrNull()
        if (income == null || income <= 0) {
            Toast.makeText(this, "Please enter a valid income.", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve the existing total income from shared preferences
        val sharedPref = this.getSharedPreferences("com.example.smartsaver.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val existingIncome = sharedPref.getFloat("income", 0.0f)

        // Add the new income to the existing total income
        val totalIncome = existingIncome + income.toFloat()

        // Save the total income in shared preferences
        with (sharedPref.edit()) {
            putFloat("income", totalIncome)
            apply()
        }

        Toast.makeText(this, "Income saved successfully!", Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_OK) // Add this line
        finish()
    }

    // Method to save the savings goal
    private fun saveGoal() {
        val goalString = etSavingsGoal.text.toString().trim()

        // Check if the goal string is empty
        if (goalString.isEmpty()) {
            Toast.makeText(this, "Please enter your savings goal.", Toast.LENGTH_SHORT).show()
            return
        }

        // Convert the goal string to a double
        val goal = goalString.toDoubleOrNull()
        if (goal == null || goal <= 0) {
            Toast.makeText(this, "Please enter a valid savings goal.", Toast.LENGTH_SHORT).show()
            return
        }

        // Save the goal in shared preferences
        val sharedPref = this.getSharedPreferences("com.example.smartsaver.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putFloat("goal", goal.toFloat())
            apply()
        }

        Toast.makeText(this, "Savings goal saved successfully!", Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_OK)
        finish()
    }
}