package com.example.smartsaver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.smartsaver.model.Payment
import com.example.smartsaver.repository.PaymentRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var tvPaymentAlert: TextView

    companion object {
        private const val CHANNEL_ID = "payments_notification_channel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvPaymentAlert = findViewById<TextView>(R.id.tvAmountDueNotification)

        createNotificationChannel()
        checkAndNotifyPayments()

        // Assuming PaymentRepository provides the necessary payment information
        val payments = PaymentRepository().getPayments()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = Calendar.getInstance()

        // Find the next payment due based on the current date
        val nextPayment = payments.minByOrNull { payment ->
            sdf.parse(payment.dueDate)?.time ?: Long.MAX_VALUE
        }

        nextPayment?.let { payment ->
            val paymentDate = sdf.parse(payment.dueDate)
            if (paymentDate != null && !paymentDate.before(today.time)) {
                tvPaymentAlert.text = "Next payment due: ${payment.description} ${payment.amount} on ${payment.dueDate}"
            } else {
                tvPaymentAlert.text = "No upcoming payments due."
            }
        } ?: run {
            tvPaymentAlert.text = "No upcoming payments due."
        }

        // Initialize buttons using findViewById
        val btnIncomeDetails = findViewById<Button>(R.id.btnIncomeDetails)
        val btnExpenseDetails = findViewById<Button>(R.id.btnExpenseDetails)
        val btnSavingsOverview = findViewById<Button>(R.id.btnSavingsOverview)
        val managePaymentsButton = findViewById<Button>(R.id.btnManagePayments)

        // Set up click listeners for each button
        btnIncomeDetails.setOnClickListener {
            startActivity(Intent(this, IncomeDetails::class.java))
        }

        btnExpenseDetails.setOnClickListener {
            startActivity(Intent(this, ExpenseDetails::class.java))
        }

        btnSavingsOverview.setOnClickListener {
            startActivity(Intent(this, SavingsOverview::class.java))
        }

        managePaymentsButton.setOnClickListener {
            startActivity(Intent(this, PaymentDetails::class.java))
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.payment_channel_name)
            val descriptionText = getString(R.string.payment_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun notifyPayment(payment: Payment) {
        val notificationId = payment.description.hashCode()  // Use the description to generate a unique ID for the notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_notification) // Ensure you have an icon resource named ic_notification in your drawable folder
            setContentTitle("Upcoming Payment Alert")
            setContentText("${payment.description}: ${payment.amount} due on ${payment.dueDate}")
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setAutoCancel(true)
        }

        // NotificationManagerCompat is used here to support older devices as well
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build()) // notificationId is a unique int for each notification
        }
    }

    private fun checkAndNotifyPayments() {
        val repository = PaymentRepository()
        val payments = repository.getPayments()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = Calendar.getInstance()

        for (payment in payments) {
            val paymentDate = sdf.parse(payment.dueDate) ?: continue
            val paymentCalendar = Calendar.getInstance()
            paymentCalendar.time = paymentDate
            if (paymentCalendar.before(today)) {
                continue
            }
            if (paymentCalendar.timeInMillis <= today.timeInMillis + 3 * 24 * 3600 * 1000) {
                notifyPayment(payment)
                break
            }
        }
    }
}
