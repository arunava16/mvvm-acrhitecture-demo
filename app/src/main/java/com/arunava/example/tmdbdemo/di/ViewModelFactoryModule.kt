package com.arunava.example.tmdbdemo.di

import androidx.lifecycle.ViewModelProvider
import com.arunava.example.tmdbdemo.viewmodelfactory.TmdbViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: TmdbViewModelFactory): ViewModelProvider.Factory
}