package com.arunava.example.tmdbdemo.ui.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arunava.example.tmdbdemo.service.TmdbRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers

class MovieListViewModel(private val repository: TmdbRepository) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _movies: MutableLiveData<HomeViewStates> by lazy { MutableLiveData() }
    val movies: LiveData<HomeViewStates> by lazy { _movies }

    fun getMovies() {
        val config = repository.getApiConfiguration().subscribeOn(Schedulers.io())
        val movies = repository.getMovies().subscribeOn(Schedulers.io())
        Singles.zip(movies, config)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _movies.value =
                    MoviesReceived(result.first.results, result.second.images)
            }, {
                _movies.value = ShowError(it.message ?: "Error, Please try again later")
            })
            .also { compositeDisposable.add(it) }

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}