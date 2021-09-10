package com.arunava.example.tmdbdemo.ui.commons.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageConfig(val baseUrl: String, val imageSize: String) : Parcelable