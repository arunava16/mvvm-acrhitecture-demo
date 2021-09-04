package com.arunava.example.tmdbdemo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arunava.example.tmdbdemo.service.TmdbRepository
import com.arunava.example.tmdbdemo.service.remote.TmdbApiClient
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import com.arunava.example.tmdbdemo.ui.movielist.MovieListActivity

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SplashViewModel(
                    TmdbRepository.getInstance(
                        RemoteDataSource.getInstance(
                            TmdbApiClient.tmdbService
                        )
                    )
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachObservers()
        splashViewModel.getConfig()
    }

    private fun attachObservers() {
        splashViewModel.apiConfig.observe(this, {
            when (it) {
                is ConfigReceived -> {
                    val intent = Intent(this, MovieListActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is ShowErrorDialog -> {
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(it.errorMsg)
                        .setMessage("Please try again later")
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .show()
                }
            }
        })
    }
}