package com.arunava.example.tmdbdemo.di

import com.arunava.example.tmdbdemo.ui.moviedetail.MovieDetailActivity
import com.arunava.example.tmdbdemo.ui.movielist.MovieListActivity
import com.arunava.example.tmdbdemo.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieListActivity(): MovieListActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailActivity(): MovieDetailActivity
}