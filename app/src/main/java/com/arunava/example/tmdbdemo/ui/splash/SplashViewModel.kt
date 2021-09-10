package com.arunava.example.tmdbdemo.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arunava.example.tmdbdemo.service.TmdbRepository
import com.arunava.example.tmdbdemo.ui.commons.data.ImageConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashViewModel(private val repository: TmdbRepository) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _apiConfig: MutableLiveData<SplashViewStates> by lazy { MutableLiveData() }
    val apiConfig: LiveData<SplashViewStates> by lazy { _apiConfig }

    fun getConfig() {
        repository.getApiConfiguration()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // TODO - Save this response to local db/cache

                // Then move to next activity
                _apiConfig.postValue(
                    ConfigReceived(
                        ImageConfig(
                            baseUrl = it.images.secureBaseUrl,
                            imageSize = it.images.posterSizes[3]
                        )
                    )
                )
            }, {
                _apiConfig.postValue(ShowErrorDialog(it.message.toString()))
            })
            .also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}