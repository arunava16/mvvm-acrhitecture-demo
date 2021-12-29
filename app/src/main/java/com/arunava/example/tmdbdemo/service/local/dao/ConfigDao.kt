package com.arunava.example.tmdbdemo.service.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arunava.example.tmdbdemo.service.local.data.Configuration

@Dao
interface ConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConfig(config: Configuration)

    @Query("SELECT * FROM configuration")
    fun getConfig(): Configuration
}