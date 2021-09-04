package com.arunava.example.tmdbdemo.service.remote

import com.arunava.example.tmdbdemo.BuildConfig
import com.arunava.example.tmdbdemo.util.LogParser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object TmdbApiClient {

    private const val baseUrl = BuildConfig.TMDB_BASE_URL

    private const val bearerToken = BuildConfig.TMDB_BEARER_TOKEN

    val tmdbService: TmdbApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(TmdbApi::class.java)

    private fun getOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor(readAccessInterceptor())
            .build()

    private fun getLoggingInterceptor() =
        HttpLoggingInterceptor(LogParser).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private fun readAccessInterceptor() = Interceptor { chain ->
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $bearerToken")
                .build()
        )
    }
}