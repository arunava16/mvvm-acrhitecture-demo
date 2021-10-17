package com.arunava.example.tmdbdemo.di

import androidx.lifecycle.ViewModel
import com.arunava.example.tmdbdemo.ui.moviedetail.MovieDetailViewModel
import com.arunava.example.tmdbdemo.ui.movielist.MovieListViewModel
import com.arunava.example.tmdbdemo.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel::class)
    abstract fun bindMovieListViewModel(viewModel: MovieListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindMovieDetailViewModel(viewModel: MovieDetailViewModel): ViewModel
}