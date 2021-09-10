package com.arunava.example.tmdbdemo.ui.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arunava.example.tmdbdemo.databinding.LayoutExhaustedItemBinding
import com.arunava.example.tmdbdemo.databinding.LayoutLoadingItemBinding
import com.arunava.example.tmdbdemo.databinding.LayoutMovieItemBinding
import com.arunava.example.tmdbdemo.ui.commons.data.ImageConfig
import com.arunava.example.tmdbdemo.ui.movielist.data.MovieItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MovieListAdapter(
    private val movies: MutableList<MovieItem> = mutableListOf(),
    private val imageConfig: ImageConfig,
    private val itemClickListener: (movie: MovieItem) -> Unit,
    private val pageScrollListener: (nextPage: Int) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE
    private var loadingNewItems = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.LOADING.ordinal ->
                LoadingViewHolder(LayoutLoadingItemBinding.inflate(inflater, parent, false))
            ViewType.MOVIE.ordinal ->
                return MovieViewHolder(LayoutMovieItemBinding.inflate(inflater, parent, false))
            ViewType.EXHAUSTED.ordinal ->
                return ExhaustedViewHolder(
                    LayoutExhaustedItemBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            else ->
                return MovieViewHolder(LayoutMovieItemBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieItem = movies[position]

        when (movieItem.viewType) {
            ViewType.LOADING -> {
                // Nothing to do, it'll show automatically
            }
            ViewType.MOVIE -> {
                (holder as MovieViewHolder).bind(movieItem)
            }
            ViewType.EXHAUSTED -> {
                // Nothing to do, it'll show automatically
            }
        }

        if (position == movies.size - 1 && !loadingNewItems && currentPage < totalPages) {
            loadingNewItems = true
            pageScrollListener.invoke(currentPage + 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return movies[position].viewType.ordinal
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun submitList(newList: List<MovieItem>, newCurrentPage: Int, newTotalPages: Int) {
        // Update flags
        loadingNewItems = false
        currentPage = newCurrentPage
        totalPages = newTotalPages

        // Insert the new range at front
        var newRangeStartingPos = 0
        var newRangeSize = newList.size
        if (movies.isEmpty()) {
            movies.addAll(newList)
            if (newList.size == MAX_ITEMS_IN_PAGE && currentPage < totalPages) {
                // If max no of items are inserted, add loading view
                movies.add(MovieItem(ViewType.LOADING))
                newRangeSize++
            }
        } else {
            movies.addAll(movies.lastIndex, newList)
            newRangeStartingPos =
                movies.size - newList.size - (if (lastViewIsLoadingView()) 1 else 0)
        }

        // notify adapter to refresh
        notifyItemRangeInserted(newRangeStartingPos, newRangeSize)
    }

    private fun lastViewIsLoadingView(): Boolean {
        return movies.last().viewType == ViewType.LOADING
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class LoadingViewHolder(binding: LayoutLoadingItemBinding) :
        ViewHolder(binding.root)

    inner class ExhaustedViewHolder(binding: LayoutExhaustedItemBinding) :
        ViewHolder(binding.root)

    inner class MovieViewHolder(private val binding: LayoutMovieItemBinding) :
        ViewHolder(binding.root) {
        fun bind(movieItem: MovieItem) {
            val posterUrl = imageConfig.baseUrl + imageConfig.imageSize + movieItem.posterPath

            Glide.with(itemView.context)
                .load(posterUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.poster)
            binding.movieName.text = movieItem.title
            binding.movieDate.text = movieItem.releaseDate
            binding.movieDescription.text = movieItem.overview

            binding.root.setOnClickListener { itemClickListener.invoke(movieItem) }
        }
    }

    enum class ViewType {
        LOADING, MOVIE, EXHAUSTED
    }

    companion object {
        private const val MAX_ITEMS_IN_PAGE = 20
    }
}