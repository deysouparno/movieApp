package com.example.movieapp.api

import android.os.Parcelable
import com.example.movieapp.data.Movie
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TmdbResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
): Parcelable