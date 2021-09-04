package com.arunava.example.tmdbdemo.service.remote

import com.arunava.example.tmdbdemo.service.data.DiscoverMovieResponse
import com.arunava.example.tmdbdemo.service.data.GetApiConfigurationResponse
import com.arunava.example.tmdbdemo.service.data.GetMovieDetailsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TmdbApi {

    @GET("discover/movie")
    fun discoverMovies(): Single<DiscoverMovieResponse>

    @GET("configuration")
    fun getApiConfiguration(): Single<GetApiConfigurationResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId: Int): Single<GetMovieDetailsResponse>
}