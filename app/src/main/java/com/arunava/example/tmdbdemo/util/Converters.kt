package com.arunava.example.tmdbdemo.util

import androidx.room.TypeConverter
import com.google.gson.Gson

object Converters {

    private val gson by lazy { Gson() }

    @TypeConverter
    fun listToJson(value: List<String>) = gson.toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = gson.fromJson(value, Array<String>::class.java).toList()
}