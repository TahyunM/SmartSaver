package com.example.smartsaver

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetails : AppCompatActivity() {

    private val sharedPrefFile = "com.example.smartsaver.PREFERENCE_FILE_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        val etExpenseAmount = findViewById<EditText>(R.id.etExpenseAmount)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)

        // Load categories and set up the spinner
        val categories = CategoryManager.getCategories(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        btnSave.setOnClickListener {
            val expenseAmountString = etExpenseAmount.text.toString()

            if (expenseAmountString.isBlank()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expenseAmount = expenseAmountString.toDoubleOrNull()
            if (expenseAmount == null || expenseAmount <= 0) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedCategory = spinnerCategory.selectedItem.toString()

            saveExpenseAmount(expenseAmount, selectedCategory)
            etExpenseAmount.text.clear()
        }
    }

    private fun saveExpenseAmount(amount: Double, category: String) {
        val sharedPref = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val totalExpense = sharedPref.getFloat("total_expense", 0.0f) + amount.toFloat()

        with(sharedPref.edit()) {
            putFloat("total_expense", totalExpense)
            apply()
        }

        Toast.makeText(this, "Expense of $amount added to category $category. Total: $totalExpense", Toast.LENGTH_LONG).show()
    }
}
