package com.arunava.example.tmdbdemo.service.remote

import com.arunava.example.tmdbdemo.service.data.DiscoverMovieResponse
import com.arunava.example.tmdbdemo.service.data.GetApiConfigurationResponse
import com.arunava.example.tmdbdemo.service.data.GetMovieDetailsResponse
import io.reactivex.Single

class RemoteDataSource private constructor(private val tmdbApi: TmdbApi) {

    fun getMovies(): Single<DiscoverMovieResponse> {
        return tmdbApi.discoverMovies()
    }

    fun getApiConfiguration() : Single<GetApiConfigurationResponse> {
        return tmdbApi.getApiConfiguration()
    }

    fun getMovieDetails(movieId: Int): Single<GetMovieDetailsResponse> {
        return tmdbApi.getMovieDetails(movieId)
    }

    companion object {
        private lateinit var instance: RemoteDataSource

        @JvmStatic
        @Synchronized
        fun getInstance(tmdbApi: TmdbApi): RemoteDataSource {
            if (!::instance.isInitialized) {
                instance = RemoteDataSource(tmdbApi)
            }
            return instance
        }
    }
}