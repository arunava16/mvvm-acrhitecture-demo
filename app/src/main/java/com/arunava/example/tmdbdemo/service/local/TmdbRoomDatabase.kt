package com.arunava.example.tmdbdemo.service.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arunava.example.tmdbdemo.service.local.dao.ConfigDao
import com.arunava.example.tmdbdemo.service.local.dao.MovieDao
import com.arunava.example.tmdbdemo.service.local.data.Configuration
import com.arunava.example.tmdbdemo.service.local.data.Movie
import com.arunava.example.tmdbdemo.util.Converters

@Database(entities = [Configuration::class, Movie::class], version = 1)
@TypeConverters(Converters::class)
abstract class TmdbRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun configDao(): ConfigDao
}