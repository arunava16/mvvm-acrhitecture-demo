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
import com.arunava.example.tmdbdemo.service.remote.RemoteDataSource
import com.arunava.example.tmdbdemo.service.remote.TmdbApiClient
import com.arunava.example.tmdbdemo.ui.moviedetail.MovieDetailActivity
import com.arunava.example.tmdbdemo.ui.movielist.data.MovieItem

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

    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerView()
        attachObservers()
        binding.progressBar.isVisible = true
        movieListViewModel.getMovies()
    }

    private fun initRecyclerView() {
        movieListAdapter = MovieListAdapter(
            imageConfig = intent.getParcelableExtra("image_config")!!, // TODO - need to look for better ways to remove not-null assertion
            itemClickListener = { movie -> onMovieItemClick(movie) },
            pageScrollListener = { page -> movieListViewModel.getMovies(page) }
        )
        binding.moviesList.adapter = movieListAdapter
    }

    private fun attachObservers() {
        movieListViewModel.movies.observe(this) { viewState ->
            when (viewState) {
                is LoadMovies -> {
                    binding.progressBar.isVisible = false
                    with(viewState) {
                        movieListAdapter.submitList(movies, currentPage, totalPages)
                    }
                }
                is ShowError -> {
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage(viewState.errorMsg)
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .show()
                }
            }
        }
    }

    private fun onMovieItemClick(movie: MovieItem) {
        val movieDetailIntent = Intent(this, MovieDetailActivity::class.java).apply {
            putExtra("movieId", movie.movieId)
        }
        startActivity(movieDetailIntent)
    }
}