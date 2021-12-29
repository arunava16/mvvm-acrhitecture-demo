package com.arunava.example.tmdbdemo.service.local.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arunava.example.tmdbdemo.service.remote.data.Images

@Entity(tableName = "configuration")
data class Configuration(

    @PrimaryKey val id: Int = 0,

    @Embedded val images: Images,

    val changeKeys: List<String>
)

