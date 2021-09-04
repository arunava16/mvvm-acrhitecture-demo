package com.arunava.example.tmdbdemo.ui.moviedetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arunava.example.tmdbdemo.databinding.ActivityMovieDetailBinding
import com.arunava.example.tmdbdemo.service.TmdbRepository
import com.arunava.example.tmdbdemo.service.data.GetMovieDetailsResponse
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import com.arunava.example.tmdbdemo.service.remote.TmdbApiClient
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MovieDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMovieDetailBinding.inflate(layoutInflater) }

    private val movieId by lazy { intent.extras?.getInt("movieId") }

    private val movieDetailViewModel: MovieDetailViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieDetailViewModel(
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
        setContentView(binding.root)

        attachObservers()
        movieId?.let { movieDetailViewModel.getMovieDetails(it) }
    }

    private fun attachObservers() {
        movieDetailViewModel.movie.observe(this) {
            when (it) {
                is MovieDetailReceived -> {
                    populateDetails(it.movieDetail, it.imagePrefix)
                }
                is ShowError -> {
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(it.errorMsg)
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun populateDetails(movieDetail: GetMovieDetailsResponse, imagePrefix: String) {
        binding.apply {
            Glide.with(this@MovieDetailActivity)
                .load(imagePrefix + movieDetail.backdropPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(backdropImage)
            movieTitle.text =
                "${movieDetail.title} (${movieDetail.releaseDate.substring(0, 4)})"
            releaseDate.text = movieDetail.releaseDate
            runningTime.text = "${movieDetail.runtime / 60}hr ${movieDetail.runtime % 60}min"
            genres.text = movieDetail.genres.joinToString { it.name }
            tagline.text = movieDetail.tagline
            movieOverview.text = movieDetail.overview
        }

    }
}