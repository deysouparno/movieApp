package com.example.movieapp.ui.movie_details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.checkForInternet
import com.example.movieapp.data.MovieDetails
import com.example.movieapp.data.MovieRepository
import com.example.movieapp.data.VideoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _videos = MutableLiveData<List<VideoData>>(mutableListOf())
    val videos: LiveData<List<VideoData>>
        get() = _videos

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails>
    get() = _movieDetails

    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    fun checkNetwork(context: Context) {
        viewModelScope.launch {
            _isConnected.postValue(checkForInternet(context = context))
        }
    }

    fun getVideos(id: Int) {
        viewModelScope.launch {
            _videos.postValue(repository.getVideos(id).results)
        }
    }

    fun getMovieDetails(id: Int) {
        viewModelScope.launch {
            _movieDetails.postValue(repository.getMovieDetails(id = id))
        }
    }
}