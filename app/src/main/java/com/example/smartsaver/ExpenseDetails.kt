package com.example.smartsaver

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetails : AppCompatActivity() {

    // Declare UI elements
    private lateinit var etExpense: EditText
    private lateinit var btnSaveExpense: Button
    private lateinit var spnCategory: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        // Initialize UI elements
        etExpense = findViewById(R.id.etExpenseAmount)
        btnSaveExpense = findViewById(R.id.btnSave)
        spnCategory = findViewById(R.id.spinnerCategory)

        // Populate the spinner with categories
        val categories = arrayOf("Food", "Transport", "Entertainment", "Bills", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spnCategory.adapter = adapter

        // Set click listener for save button
        btnSaveExpense.setOnClickListener { saveExpense() }
    }

    private fun saveExpense() {
        // Get the entered expense as a string
        val expenseString = etExpense.text.toString().trim()

        // Check if the expense string is empty
        if (expenseString.isEmpty()) {
            Toast.makeText(this, "Please enter your expense.", Toast.LENGTH_SHORT).show()
            return
        }

        // Convert the expense string to a double
        val expense = expenseString.toDoubleOrNull()
        // Check if the expense is null or less than or equal to 0
        if (expense == null || expense <= 0) {
            Toast.makeText(this, "Please enter a valid expense.", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the selected category
        val category = spnCategory.selectedItem.toString()

        // Save the expense in shared preferences
        val sharedPref = this.getSharedPreferences("com.example.smartsaver.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val existingExpense = sharedPref.getFloat(category, 0.0f)
        val totalExpense = existingExpense + expense.toFloat()

        // Update the total expense for the selected category
        with (sharedPref.edit()) {
            putFloat(category, totalExpense)
            apply()
        }

        // Show a success message
        Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_OK)
        finish()
    }
}