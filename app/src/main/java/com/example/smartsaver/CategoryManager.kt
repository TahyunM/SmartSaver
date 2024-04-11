package com.example.smartsaver

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Category(val id: Int, val name: String)

object CategoryManager {
    private const val PREFS_NAME = "categories_prefs"
    private const val CATEGORIES_KEY = "categories"

    fun saveCategories(context: Context, categories: List<Category>) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(categories)
        editor.putString(CATEGORIES_KEY, json)
        editor.apply()
    }

    fun getCategories(context: Context): List<Category> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(CATEGORIES_KEY, null) ?: return emptyList()
        val gson = Gson()
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addCategory(context: Context, categoryName: String) {
        val currentCategories = getCategories(context)
        val newId = (currentCategories.maxByOrNull { it.id }?.id ?: 0) + 1
        val newCategory = Category(newId, categoryName)
        val updatedCategories = currentCategories + newCategory
        saveCategories(context, updatedCategories)
    }
}
