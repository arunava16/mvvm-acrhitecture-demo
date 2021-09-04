package com.arunava.example.tmdbdemo.service

import com.arunava.example.tmdbdemo.service.data.DiscoverMovieResponse
import com.arunava.example.tmdbdemo.service.data.GetApiConfigurationResponse
import com.arunava.example.tmdbdemo.service.data.GetMovieDetailsResponse
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import io.reactivex.Single

class TmdbRepository private constructor(private val remoteDataSource: RemoteDataSource) {

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

    fun getMovies(): Single<DiscoverMovieResponse> {
        return remoteDataSource.getMovies()
    }

    fun getMovieDetails(movieId: Int): Single<GetMovieDetailsResponse> {
        return remoteDataSource.getMovieDetails(movieId)
    }

    companion object {

        private lateinit var instance: TmdbRepository

        @JvmStatic
        @Synchronized
        fun getInstance(remoteDataSource: RemoteDataSource): TmdbRepository {
            if (!::instance.isInitialized) {
                instance = TmdbRepository(remoteDataSource)
            }
            return instance
        }
    }
}