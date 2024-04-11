package com.example.smartsaver

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IncomeDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_details)

        val etIncomeAmount = findViewById<EditText>(R.id.etIncomeAmount)
        val etSavingsGoal = findViewById<EditText>(R.id.etSavingsGoal)
        val btnSaveIncome = findViewById<Button>(R.id.btnSaveIncome)
        val tvMonthlyBudget = findViewById<TextView>(R.id.tvMonthlyBudget)

        btnSaveIncome.setOnClickListener {
            val incomeAmountString = etIncomeAmount.text.toString()
            val savingsGoalString = etSavingsGoal.text.toString()

            if (incomeAmountString.isBlank() || savingsGoalString.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val incomeAmount = incomeAmountString.toDoubleOrNull()
            val savingsGoal = savingsGoalString.toDoubleOrNull()
            if (incomeAmount == null || incomeAmount <= 0 || savingsGoal == null || savingsGoal <= 0) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveIncomeAndGoal(incomeAmount, savingsGoal)
            calculateAndDisplayBudget(incomeAmount, savingsGoal, tvMonthlyBudget)
        }
    }

    private fun saveIncomeAndGoal(income: Double, goal: Double) {
        val sharedPreferences = this.getSharedPreferences("IncomeDetailsPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("income_amount", income.toFloat())
        editor.putFloat("savings_goal", goal.toFloat())
        editor.apply()
    }


    private fun calculateAndDisplayBudget(income: Double, savingsGoal: Double, budgetDisplay: TextView) {
        val monthlyBudget = income - savingsGoal
        budgetDisplay.text = "Monthly Budget: $monthlyBudget"
    }
}
