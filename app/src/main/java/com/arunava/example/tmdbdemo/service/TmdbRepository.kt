package com.arunava.example.tmdbdemo.service

import com.arunava.example.tmdbdemo.service.data.DiscoverMovieResponse
import com.arunava.example.tmdbdemo.service.data.GetApiConfigurationResponse
import com.arunava.example.tmdbdemo.service.data.GetMovieDetailsResponse
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import io.reactivex.Single
import javax.inject.Inject

class TmdbRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    private var apiConfigurationResponse: GetApiConfigurationResponse? = null

    fun getApiConfiguration(): Single<GetApiConfigurationResponse> {
        return if (apiConfigurationResponse != null) {
            Single.just(apiConfigurationResponse)
        } else {
            remoteDataSource.getApiConfiguration().doOnSuccess {
                apiConfigurationResponse = it
            }
        }
    }

    fun getMovies(page: Int): Single<DiscoverMovieResponse> {
        return remoteDataSource.getMovies(page)
    }

    fun getMovieDetails(movieId: Int): Single<GetMovieDetailsResponse> {
        return remoteDataSource.getMovieDetails(movieId)
    }
}