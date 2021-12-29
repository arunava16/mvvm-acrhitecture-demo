package com.arunava.example.tmdbdemo.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arunava.example.tmdbdemo.service.repository.Resource
import com.arunava.example.tmdbdemo.service.repository.TmdbRepository
import com.arunava.example.tmdbdemo.ui.commons.data.ImageConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val repository: TmdbRepository) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _viewState: MutableLiveData<SplashViewStates> by lazy { MutableLiveData() }
    val viewState: LiveData<SplashViewStates> by lazy { _viewState }

    fun getConfig() {
        repository.getApiConfiguration()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // We don't need this here
                    }
                    is Resource.Success -> {
                        resource.data?.let {
                            _viewState.postValue(
                                SplashViewStates.ConfigReceived(
                                    ImageConfig(
                                        resource.data.images.secureBaseUrl,
                                        resource.data.images.posterSizes[3]
                                    )
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _viewState.postValue(SplashViewStates.ShowErrorDialog(resource.message.toString()))
                    }
                }
            }
            .also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}