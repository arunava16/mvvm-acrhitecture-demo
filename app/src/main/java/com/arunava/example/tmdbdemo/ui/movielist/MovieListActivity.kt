package com.arunava.example.tmdbdemo.ui.movielist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arunava.example.tmdbdemo.databinding.ActivityMovieListBinding
import com.arunava.example.tmdbdemo.service.TmdbRepository
import com.arunava.example.tmdbdemo.service.data.Results
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import com.arunava.example.tmdbdemo.service.remote.TmdbApiClient
import com.arunava.example.tmdbdemo.ui.moviedetail.MovieDetailActivity

class MovieListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMovieListBinding.inflate(layoutInflater) }

    private val movieListViewModel: MovieListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieListViewModel(
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
        binding.progressBar.isVisible = true
        movieListViewModel.getMovies()
    }

    private fun attachObservers() {
        movieListViewModel.movies.observe(this) {
            when (it) {
                is MoviesReceived -> {
                    binding.progressBar.isVisible = false
                    binding.moviesList.adapter =
                        MovieListAdapter(this, it.movies, it.imageConfig) { movie ->
                            onMovieItemClick(movie)
                        }
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

    private fun onMovieItemClick(movie: Results) {
        val movieDetailIntent = Intent(this, MovieDetailActivity::class.java).apply {
            putExtra("movieId", movie.id)
        }
        startActivity(movieDetailIntent)
    }
}