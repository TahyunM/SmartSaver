package com.example.smartsaver

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

// This activity provides an overview of the user's savings
class SavingsOverview : AppCompatActivity() {
    // Declare UI elements
    private lateinit var pieChartIncomeExpenses: PieChart
    private lateinit var pieChartCategoryExpenses: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_overview)

        // Initialize UI elements
        pieChartIncomeExpenses = findViewById(R.id.pieChartIncomeExpenses)
        pieChartCategoryExpenses = findViewById(R.id.pieChartCategoryExpenses)

        // Display the financial overview
        displayFinancialOverview()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the financial overview when the activity resumes
        displayFinancialOverview()
    }

    // Method to display the financial overview
    private fun displayFinancialOverview() {
        // Get the shared preferences
        val sharedPref = this.getSharedPreferences(
            "com.example.smartsaver.PREFERENCE_FILE_KEY",
            Context.MODE_PRIVATE
        )
        // Get the total income from the shared preferences
        val totalIncome = sharedPref.getFloat("income", 0.0f)

        // Calculate total expenses
        val categories = arrayOf("Food", "Transport", "Entertainment", "Bills", "Other")
        var totalExpenses = 0.0f
        val categoryEntries = ArrayList<PieEntry>()
        for (category in categories) {
            // Get the expense for each category and add it to the total expenses
            val categoryExpense = sharedPref.getFloat(category, 0.0f)
            totalExpenses += categoryExpense
            // Add an entry for each category to the category entries
            categoryEntries.add(PieEntry(categoryExpense, category))
        }

        // Create the entries for the income and expenses pie chart
        val incomeExpenseEntries = ArrayList<PieEntry>().apply {
            add(PieEntry(totalIncome, "Income"))
            add(PieEntry(totalExpenses, "Expenses"))
        }

        // Create the data set and data for the income and expenses pie chart
        val incomeExpenseDataSet = PieDataSet(incomeExpenseEntries, "Income and Expenses")
        incomeExpenseDataSet.colors = listOf(
            resources.getColor(R.color.colorIncome, null),
            resources.getColor(R.color.colorExpenses, null)
        )
        val incomeExpenseData = PieData(incomeExpenseDataSet)

        // Update the income and expenses pie chart
        pieChartIncomeExpenses.data = incomeExpenseData
        pieChartIncomeExpenses.setDrawEntryLabels(false)
        pieChartIncomeExpenses.invalidate()

        // Create the data set and data for the category expenses pie chart
        val categoryDataSet = PieDataSet(categoryEntries, "Category Expenses")
        categoryDataSet.colors = listOf(
            resources.getColor(R.color.colorFood, null),
            resources.getColor(R.color.colorTransport, null),
            resources.getColor(R.color.colorEntertainment, null),
            resources.getColor(R.color.colorBills, null),
            resources.getColor(R.color.colorOther, null)
        )
        val categoryData = PieData(categoryDataSet)

        // Update the category expenses pie chart
        pieChartCategoryExpenses.data = categoryData
        pieChartCategoryExpenses.setDrawEntryLabels(false)
        pieChartCategoryExpenses.invalidate()
    }
}