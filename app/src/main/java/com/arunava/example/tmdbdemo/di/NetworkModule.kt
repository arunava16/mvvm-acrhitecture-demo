package com.arunava.example.tmdbdemo.di

import com.arunava.example.tmdbdemo.BuildConfig
import com.arunava.example.tmdbdemo.service.TmdbRepository
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import com.arunava.example.tmdbdemo.service.remote.TmdbApi
import com.arunava.example.tmdbdemo.util.LogParser
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    private const val baseUrl = BuildConfig.TMDB_BASE_URL

    private const val bearerToken = BuildConfig.TMDB_BEARER_TOKEN

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): TmdbRepository =
        TmdbRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideRemoteDataSource(tmdbApi: TmdbApi): RemoteDataSource = RemoteDataSource(tmdbApi)

    @Provides
    @Singleton
    fun provideTmdbApi(retrofit: Retrofit): TmdbApi = retrofit.create(TmdbApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        readAccessInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(readAccessInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(LogParser).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideReadAccessInterceptor(): Interceptor {
        return Interceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $bearerToken")
                    .build()
            )
        }
    }
}