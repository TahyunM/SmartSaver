package com.example.smartsaver

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CategoryInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_input)

        val buttonSaveCategory = findViewById<Button>(R.id.buttonSaveCategory)
        val editTextCategoryName = findViewById<EditText>(R.id.editTextCategoryName)

        buttonSaveCategory.setOnClickListener {
            val categoryName = editTextCategoryName.text.toString()
            if (categoryName.isNotEmpty()) {
                CategoryManager.addCategory(this, categoryName)
                Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show()
                finish()  // Optionally close the activity
            } else {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
