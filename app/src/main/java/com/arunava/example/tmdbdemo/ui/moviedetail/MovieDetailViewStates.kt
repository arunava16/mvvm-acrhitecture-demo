package com.arunava.example.tmdbdemo.ui.moviedetail

import com.arunava.example.tmdbdemo.service.data.GetMovieDetailsResponse

sealed class MovieDetailViewStates

data class MovieDetailReceived(
    val movieDetail: GetMovieDetailsResponse,
    val imagePrefix: String
) : MovieDetailViewStates()

data class ShowError(val errorMsg: String) : MovieDetailViewStates()