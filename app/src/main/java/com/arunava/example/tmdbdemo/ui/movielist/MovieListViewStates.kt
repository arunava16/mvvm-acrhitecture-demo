package com.arunava.example.tmdbdemo.ui.movielist

import com.arunava.example.tmdbdemo.service.data.Images
import com.arunava.example.tmdbdemo.service.data.Results

sealed class HomeViewStates

data class MoviesReceived(val movies: List<Results>, val imageConfig: Images) : HomeViewStates()
data class ShowError(val errorMsg: String) : HomeViewStates()