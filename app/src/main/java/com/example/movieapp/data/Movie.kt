package com.example.movieapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val adult: Boolean,
    val backdrop_path: String?,
    val poster_path: String?,
    val overview: String,
    val release_date: String,

): Parcelable

@Parcelize
data class Genre(
    val id: Int,
    val name: String
): Parcelable

@Parcelize
data class ProductionCompany(
    val id: Int,
    val name: String,
    val logo_path: String
): Parcelable

@Parcelize
data class MovieDetails(
    val id: Int,
    val title: String,
    val tagline: String,
    val adult: Boolean,
    val backdrop_path: String?,
    val poster_path: String?,
    val genres: List<Genre>,
    val overview: String,
    val release_date: String,
    val runtime: Int,
    val vote_average: Float,
): Parcelable
