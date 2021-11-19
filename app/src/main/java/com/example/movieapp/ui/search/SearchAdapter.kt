package com.example.movieapp.ui.search

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movieapp.R
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.ui.ClickListener

class SearchAdapter(private val listener: ClickListener) :
    PagingDataAdapter<Movie, SearchViewHolder>(diffCallback = diffUtilCallback) {

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) holder.bind(currentItem)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = SearchViewHolder(binding)
//        binding.movieImage.setOnClickListener {
//            getItem(viewHolder.bindingAdapterPosition)?.let { it1 -> listener.onClick(it1) }
//        }

        var shouldClick = true
        var milli = 0L


        binding.movieImage.setOnTouchListener { v, event ->
            return@setOnTouchListener when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    shouldClick = true
                    milli = System.currentTimeMillis()
                    Log.d("home", "millis is initiated $milli")
                    binding.root.animate().apply {
                        duration = 200
                        scaleX(0.7f)
                        scaleY(0.7f)
                    }
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    shouldClick = false
                    binding.root.animate().apply {
                        duration = 100
                        scaleX(1f)
                        scaleY(1f)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    binding.root.animate().apply {
                        duration = 100
                        scaleX(1f)
                        scaleY(1f)
                    }
                    if (shouldClick && System.currentTimeMillis() - milli <= 1000) {
                        getItem(viewHolder.bindingAdapterPosition)?.let { it1 ->
                            listener.onClick(
                                it1,
                                binding.movieImage
                            )
                        }
                    }
                    true
                }
                else -> false
            }
        }
        return viewHolder
    }


}

val diffUtilCallback = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return newItem == oldItem
    }

}

class SearchViewHolder(private val itemBinding: MovieItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(movie: Movie) {
        itemBinding.apply {
            Glide.with(movieImage.context)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_baseline_movie_24)
                .into(movieImage)

            movieName.text = movie.title
        }
    }
}
