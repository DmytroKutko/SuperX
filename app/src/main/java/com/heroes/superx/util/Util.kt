package com.heroes.superx.util

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.heroes.superx.models.Hero


fun parseJsonToData(result: String): List<Hero> {
    val exportType = object : TypeToken<List<Hero>>() {}.type
    return try {
        Gson().fromJson(result, exportType)
    } catch (e: Exception) {
        Log.e("From Json Exception", "$e")
        emptyList()
    }
}