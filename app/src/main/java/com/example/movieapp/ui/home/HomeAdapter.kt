package com.example.movieapp.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movieapp.R
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.ui.ClickListener

class HomeAdapter(private val listener: ClickListener, private val code: String) :
    RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.form(parent, listener, differ, code = code)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        if (position == differ.currentList.size) {
            holder.bindLastItem()
            return
        }
        val movie = differ.currentList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return if (differ.currentList.size == 0)
            differ.currentList.size
        else
            differ.currentList.size + 1
    }


    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Movie>) {
        differ.submitList(list)
    }

}

private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}

class HomeViewHolder(private val binding: MovieItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        @SuppressLint("ClickableViewAccessibility")
        fun form(
            parent: ViewGroup,
            listener: ClickListener,
            differ: AsyncListDiffer<Movie>,
            code: String
        ): HomeViewHolder {
            val binding =
                MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val viewHolder = HomeViewHolder(binding)

            var shouldClick = true
            var milli = 0L

            binding.movieImage.setOnTouchListener { v, event ->
                return@setOnTouchListener when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        shouldClick = true
                        milli = System.currentTimeMillis()
                        Log.d("home", "millis is initiated $milli")
                        binding.root.animate().apply {
                            duration = 100
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
                            duration = 200
                            scaleX(1f)
                            scaleY(1f)
                        }
                        if (shouldClick && System.currentTimeMillis() - milli <= 1000) {
                            if (differ.currentList.size == viewHolder.bindingAdapterPosition) {
                                listener.viewMore(code = code)
                            } else {
                                listener.onClick(differ.currentList[viewHolder.bindingAdapterPosition], binding.movieImage)
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
/*
            binding.movieImage.setOnClickListener {
                binding.movieImage.animate().apply {
                    duration = 200
                    scaleX(0.7f)
                    scaleY(0.7f)
                }.withEndAction {
                    binding.movieImage.animate().apply {
                        duration = 200
                        scaleX(1f)
                        scaleY(1f)
                    }
                }
                if (differ.currentList.size == viewHolder.bindingAdapterPosition) {
                    listener.viewMore(code = code)
                } else {
                    listener.onClick(differ.currentList[viewHolder.bindingAdapterPosition])
                }
            }

 */
            return viewHolder
        }
    }

    fun bind(movie: Movie) {
        binding.apply {
            binding.movieName.text = movie.title
            Glide.with(movieImage.context)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_baseline_movie_24)
                .into(movieImage)
        }
    }

    fun bindLastItem() {
        binding.apply {
            Glide.with(movieImage.context)
                .load(R.drawable.view_all)
                .into(movieImage)
            binding.movieName.text = "view more"
        }
    }
}