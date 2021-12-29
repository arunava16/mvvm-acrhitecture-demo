package com.arunava.example.tmdbdemo.ui.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arunava.example.tmdbdemo.service.repository.TmdbRepository
import com.arunava.example.tmdbdemo.ui.movielist.data.MovieItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieListViewModel @Inject constructor(
    private val repository: TmdbRepository
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _movies: MutableLiveData<HomeViewStates> by lazy { MutableLiveData() }
    val movies: LiveData<HomeViewStates> by lazy { _movies }

    fun getMovies(page: Int = 1) {
        repository.getMovies(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                _movies.value = LoadMovies(
                    movies = response.results.map {
                        MovieItem(
                            viewType = MovieListAdapter.ViewType.MOVIE,
                            movieId = it.id,
                            title = it.title,
                            releaseDate = it.releaseDate,
                            overview = it.overview,
                            posterPath = it.posterPath
                        )
                    },
                    currentPage = response.page,
                    totalPages = response.totalPages
                )
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