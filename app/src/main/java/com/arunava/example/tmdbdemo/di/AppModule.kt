package com.arunava.example.tmdbdemo.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.arunava.example.tmdbdemo.BuildConfig
import com.arunava.example.tmdbdemo.service.local.TmdbRoomDatabase
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import com.arunava.example.tmdbdemo.service.remote.TmdbApi
import com.arunava.example.tmdbdemo.service.repository.TmdbRepository
import com.arunava.example.tmdbdemo.util.LogParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val baseUrl = BuildConfig.TMDB_BASE_URL

    private const val bearerToken = BuildConfig.TMDB_BEARER_TOKEN

    @Provides
    fun provideAppContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        tmdbDatabase: TmdbRoomDatabase
    ): TmdbRepository = TmdbRepository(remoteDataSource, tmdbDatabase)

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

    @Provides
    @Singleton
    fun provideTmdbDatabase(application: Application): TmdbRoomDatabase =
        Room.databaseBuilder(application, TmdbRoomDatabase::class.java, "db")
            .fallbackToDestructiveMigration()
            .build()
}
