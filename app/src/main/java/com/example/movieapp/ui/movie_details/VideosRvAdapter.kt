package com.example.movieapp.ui.movie_details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.data.VideoData
import com.example.movieapp.databinding.VideoItemBinding
import com.example.movieapp.ui.VideoClickListener

class VideosRvAdapter(private val movieName: String, private val listener: VideoClickListener) : RecyclerView.Adapter<VideoViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder.form(parent, listener, differ)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(differ.currentList[position], movieName)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<VideoData>) {
        differ.submitList(list)
    }
}

private val diffCallback = object : DiffUtil.ItemCallback<VideoData>() {
    override fun areItemsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
        return oldItem == newItem
    }

}

class VideoViewHolder(private val binding: VideoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun form(parent: ViewGroup, listener: VideoClickListener, differ: AsyncListDiffer<VideoData>): VideoViewHolder {
            val binding =
                VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val viewHolder = VideoViewHolder(binding)
            binding.imageView.setOnClickListener {
                listener.onClick(differ.currentList[viewHolder.bindingAdapterPosition])
            }
            binding.videoContainer.setOnClickListener {
                listener.onClick(differ.currentList[viewHolder.bindingAdapterPosition])
            }
            return viewHolder
        }
    }

    fun bind(video: VideoData, name: String) {
        binding.apply {
            Glide.with(videoThumbnail.context)
                .load("https://img.youtube.com/vi/${video.key}/hqdefault.jpg")
                .centerCrop()
                .into(videoThumbnail)

            videoName.text = video.name
            movieName.text = name
        }
    }

}