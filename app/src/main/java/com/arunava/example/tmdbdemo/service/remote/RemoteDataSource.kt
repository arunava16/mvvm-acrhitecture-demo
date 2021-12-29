package com.arunava.example.tmdbdemo.service.remote

import com.arunava.example.tmdbdemo.service.remote.data.DiscoverMovieResponse
import com.arunava.example.tmdbdemo.service.remote.data.GetApiConfigurationResponse
import com.arunava.example.tmdbdemo.service.remote.data.GetMovieDetailsResponse
import io.reactivex.Single
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val tmdbApi: TmdbApi) {

    fun getMovies(page: Int): Single<DiscoverMovieResponse> {
        return tmdbApi.discoverMovies(page)
    }

    fun getApiConfiguration(): Single<GetApiConfigurationResponse> {
        return tmdbApi.getApiConfiguration()
    }

    fun getMovieDetails(movieId: Int): Single<GetMovieDetailsResponse> {
        return tmdbApi.getMovieDetails(movieId)
    }
}