package com.arunava.example.tmdbdemo.ui.movielist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arunava.example.tmdbdemo.databinding.LayoutMovieItemBinding
import com.arunava.example.tmdbdemo.service.data.Images
import com.arunava.example.tmdbdemo.service.data.Results

class MovieListAdapter(
    private val context: Context,
    private val movies: List<Results>,
    private val imageConfig: Images,
    private val itemClickListener: (Results) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutMovieItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        val posterUrl = imageConfig.secureBaseUrl + imageConfig.posterSizes[3] + movie.posterPath

        holder.binding.poster.load(posterUrl) {
            crossfade(true)
        }
        holder.binding.movieName.text = movie.title
        holder.binding.movieDate.text = movie.releaseDate
        holder.binding.movieDescription.text = movie.overview

        holder.binding.root.setOnClickListener { itemClickListener.invoke(movie) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(val binding: LayoutMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}