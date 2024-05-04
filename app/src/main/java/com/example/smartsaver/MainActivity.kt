package com.example.smartsaver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

// MainActivity is the first screen that users see when they open the app
class MainActivity : AppCompatActivity() {
    // Declare UI elements
    private lateinit var tvPaymentAlert: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvUpcomingPayment: TextView

    // Declare activity result launchers for income, expense, and payment details
    private val incomeResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // If the result is OK, calculate and display the total amount
            if (result.resultCode == Activity.RESULT_OK) {
                calculateAndDisplayTotalAmount()
            }
        }

    private val expenseResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // If the result is OK, calculate and display the total amount
            if (result.resultCode == Activity.RESULT_OK) {
                calculateAndDisplayTotalAmount()
            }
        }
    private val paymentResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // If the result is OK, display the upcoming payment
            if (result.resultCode == Activity.RESULT_OK) {
                displayUpcomingPayment()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvUpcomingPayment = findViewById(R.id.tvUpcomingPayment)

        val btnIncomeDetails = findViewById<Button>(R.id.btnIncomeDetails)
        val btnExpenseDetails = findViewById<Button>(R.id.btnExpenseDetails)
        val btnSavingsOverview = findViewById<Button>(R.id.btnSavingsOverview)
        val managePaymentsButton = findViewById<Button>(R.id.btnManagePayments)

        // Set click listeners for the buttons
        btnIncomeDetails.setOnClickListener {
            // Open the IncomeDetails activity when the button is clicked
            val intent = Intent(this, IncomeDetails::class.java)
            incomeResultLauncher.launch(intent)
        }

        btnExpenseDetails.setOnClickListener {
            // Open the ExpenseDetails activity when the button is clicked
            val intent = Intent(this, ExpenseDetails::class.java)
            expenseResultLauncher.launch(intent)
        }

        btnSavingsOverview.setOnClickListener {
            // Open the SavingsOverview activity when the button is clicked
            startActivity(Intent(this, SavingsOverview::class.java))
        }

        managePaymentsButton.setOnClickListener {
            // Open the PaymentDetails activity when the button is clicked
            val intent = Intent(this, PaymentDetails::class.java)
            paymentResultLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Calculate and display the total amount and the upcoming payment when the activity resumes
        calculateAndDisplayTotalAmount()
        displayUpcomingPayment()
    }

    // Method to calculate and display the total amount
    private fun calculateAndDisplayTotalAmount() {
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
        for (category in categories) {
            // Get the expense for each category and add it to the total expenses
            totalExpenses += sharedPref.getFloat(category, 0.0f)
        }

        // Calculate the total amount by subtracting the total expenses from the total income
        val totalAmount = totalIncome - totalExpenses
        // Display the total amount
        tvTotalAmount.text = "Total Amount: $totalAmount"
    }

    // Method to display the upcoming payment
    private fun displayUpcomingPayment() {
        // Get the shared preferences
        val sharedPref = this.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        // Get the payments from the shared preferences
        val json = sharedPref.getString("payments", null)
        if (json != null) {
            // Convert the JSON string to a list of Payment objects
            val payments: List<Payment> =
                Gson().fromJson(json, object : TypeToken<List<Payment>>() {}.type)
            if (payments.isNotEmpty()) {
                // Sort the payments by due date
                val sortedPayments = payments.sortedBy {
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).parse(it.dueDate)
                }
                // Get the upcoming payment (the first one in the sorted list)
                val upcomingPayment = sortedPayments[0]
                // Display the upcoming payment
                tvUpcomingPayment.text =
                    "Upcoming Payment: ${upcomingPayment.description}, Amount: ${upcomingPayment.amount}, Date: ${upcomingPayment.dueDate}"
            } else {
                // If there are no payments, display a message saying so
                tvUpcomingPayment.text = "No upcoming payments."
            }
        } else {
            // If there are no payments, display a message saying so
            tvUpcomingPayment.text = "No upcoming payments."
        }
    }
}