package com.example.movieapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoResponse(
    val id: Int,
    val results: List<VideoData>
): Parcelable


@Parcelize
data class VideoData(
    val id: String,
    val key: String,
    val site: String,
    val name: String,
    val type: String,
): Parcelable