package com.arunava.example.tmdbdemo.service.repository

import com.arunava.example.tmdbdemo.service.local.TmdbRoomDatabase
import com.arunava.example.tmdbdemo.service.local.data.Configuration
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import com.arunava.example.tmdbdemo.service.remote.data.DiscoverMovieResponse
import com.arunava.example.tmdbdemo.service.remote.data.GetApiConfigurationResponse
import com.arunava.example.tmdbdemo.service.remote.data.GetMovieDetailsResponse
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val tmdbDatabase: TmdbRoomDatabase
) {

    fun getApiConfiguration(): Observable<Resource<Configuration>> {
        return object : NetworkBoundResource<Configuration, GetApiConfigurationResponse>() {
            override fun networkFetch(): Single<GetApiConfigurationResponse> {
                return remoteDataSource.getApiConfiguration()
            }

            override fun saveFetchResult(data: GetApiConfigurationResponse) {
                val config = Configuration(images = data.images, changeKeys = data.changeKeys)
                tmdbDatabase.configDao().insertConfig(config)
            }

            override fun loadFromDb(): Configuration {
                return tmdbDatabase.configDao().getConfig()
            }

            override fun shouldFetch(data: Configuration?): Boolean {
                return data == null
            }
        }.getResource()
    }

    fun getMovies(page: Int): Single<DiscoverMovieResponse> {
        return remoteDataSource.getMovies(page)
    }

    fun getMovieDetails(movieId: Int): Single<GetMovieDetailsResponse> {
        return remoteDataSource.getMovieDetails(movieId)
    }
}