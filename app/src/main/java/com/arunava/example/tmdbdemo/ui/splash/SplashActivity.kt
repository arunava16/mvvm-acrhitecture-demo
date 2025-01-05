package com.arunava.example.tmdbdemo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.arunava.example.tmdbdemo.ui.commons.data.ImageConfig
import com.arunava.example.tmdbdemo.ui.movielist.MovieListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachObservers()
        splashViewModel.getConfig()
    }

    private fun attachObservers() {
        splashViewModel.viewState.observe(this) {
            when (it) {
                is SplashViewStates.ConfigReceived -> navigateToMovieList(it.imageConfig)
                is SplashViewStates.ShowErrorDialog -> showErrorDialog(it.errorMsg)
            }
        }
    }

    private fun navigateToMovieList(imageConfig: ImageConfig) {
        val intent = Intent(this, MovieListActivity::class.java)
        intent.putExtra("image_config", imageConfig)
        startActivity(intent)
        finish()
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(errorMessage)
            .setMessage("Please try again later")
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }
}
