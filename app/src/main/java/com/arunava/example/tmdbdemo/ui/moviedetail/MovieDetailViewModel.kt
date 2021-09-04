package com.arunava.example.tmdbdemo.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arunava.example.tmdbdemo.service.TmdbRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers

class MovieDetailViewModel(private val repository: TmdbRepository) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _movie: MutableLiveData<MovieDetailViewStates> by lazy { MutableLiveData() }
    val movie: LiveData<MovieDetailViewStates> by lazy { _movie }

    fun getMovieDetails(movieId: Int) {
        val config = repository.getApiConfiguration().subscribeOn(Schedulers.io())
        val movieDetail = repository.getMovieDetails(movieId).subscribeOn(Schedulers.io())
        Singles.zip(movieDetail, config)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _movie.value = MovieDetailReceived(
                    movieDetail = it.first,
                    imagePrefix = it.second.images.run { secureBaseUrl + backdropSizes[2] }
                )
            }, {
                _movie.value = ShowError(it.message ?: "Error, Please try again later")
            })
            .also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}