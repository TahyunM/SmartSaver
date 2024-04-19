package com.example.smartsaver

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Payment(
    val description: String,
    val amount: Double,
    val dueDate: String // Date in the format "dd/MM/yyyy"
)

class PaymentDetails : AppCompatActivity() {

    private lateinit var etDescription: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDueDate: EditText
    private lateinit var btnSavePayment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_details)

        etDescription = findViewById(R.id.etPaymentDescription)
        etAmount = findViewById(R.id.etPaymentAmount)
        etDueDate = findViewById(R.id.etPaymentDate)
        btnSavePayment = findViewById(R.id.btnSavePayment)

        etDueDate.setOnClickListener { showDatePickerDialog() }
        btnSavePayment.setOnClickListener { savePayment() }
    }

    private fun savePayment() {
        val description = etDescription.text.toString().trim()
        val amountString = etAmount.text.toString().trim()
        val dueDate = etDueDate.text.toString().trim()

        if (description.isEmpty() || amountString.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountString.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
            return
        }

        val newPayment = Payment(description, amount, dueDate)
        val payments = getPayments() + newPayment
        savePayments(payments)
        Toast.makeText(this, "Payment saved successfully!", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year)
            etDueDate.setText(selectedDate)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun savePayments(payments: List<Payment>) {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(payments)
        editor.putString("payments", json)
        editor.apply()
    }

    private fun getPayments(): List<Payment> {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("payments", null) ?: return emptyList()
        val type = object : TypeToken<List<Payment>>() {}.type
        return Gson().fromJson(json, type)
    }
}
