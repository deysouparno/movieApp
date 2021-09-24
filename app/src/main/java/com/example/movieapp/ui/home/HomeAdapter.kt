package com.example.movieapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.R
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.ui.ClickListener

class HomeAdapter(private val listener: ClickListener, private val code: String): RecyclerView.Adapter<HomeViewHolder>() {

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

class HomeViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun form(parent: ViewGroup, listener: ClickListener, differ: AsyncListDiffer<Movie>, code: String): HomeViewHolder {
            val binding =
                MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val viewHolder = HomeViewHolder(binding)

            binding.root.setOnClickListener {
                if (differ.currentList.size  == viewHolder.bindingAdapterPosition) {
                    listener.viewMore(code = code)
                }
                else {
                    listener.onClick(differ.currentList[viewHolder.bindingAdapterPosition])
                }
            }
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