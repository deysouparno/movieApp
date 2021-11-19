package com.example.movieapp.ui

import android.widget.ImageView
import com.example.movieapp.data.Movie
import com.example.movieapp.data.VideoData

interface ClickListener {
    fun onClick(movie: Movie, movieImg: ImageView)
    fun viewMore(code: String)
}

interface VideoClickListener {
    fun onClick(video: VideoData)
}