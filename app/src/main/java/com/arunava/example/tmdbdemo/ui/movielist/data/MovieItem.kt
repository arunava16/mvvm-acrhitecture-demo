package com.arunava.example.tmdbdemo.ui.movielist.data

import com.arunava.example.tmdbdemo.ui.movielist.MovieListAdapter

data class MovieItem(
    val viewType: MovieListAdapter.ViewType,
    val movieId: Int? = null,
    val title: String? = null,
    val releaseDate: String? = null,
    val overview: String? = null,
    val posterPath: String? = null
)