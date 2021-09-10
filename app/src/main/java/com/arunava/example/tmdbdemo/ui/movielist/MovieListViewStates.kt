package com.arunava.example.tmdbdemo.ui.movielist

import com.arunava.example.tmdbdemo.ui.movielist.data.MovieItem

sealed class HomeViewStates

data class LoadMovies(
    val movies: List<MovieItem>,
    val currentPage: Int,
    val totalPages: Int
) : HomeViewStates()

data class ShowError(val errorMsg: String) : HomeViewStates()