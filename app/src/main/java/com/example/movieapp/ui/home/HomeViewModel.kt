package com.example.movieapp.ui.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {

    private val _popularMovies = MutableLiveData<List<Movie>>(mutableListOf())
    val popularMovies: LiveData<List<Movie>>
    get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<List<Movie>>(mutableListOf())
    val topRatedMovies: LiveData<List<Movie>>
        get() = _topRatedMovies

    private val _latestMovies = MutableLiveData<List<Movie>>(mutableListOf())
    val latestMovies: LiveData<List<Movie>>
        get() = _latestMovies

    fun loadPopularMovies() {
        viewModelScope.launch {
            _popularMovies.postValue(repository.getPopularMovies().results)
        }
    }

    fun loadTopRatedMovies() {
        viewModelScope.launch {
            _topRatedMovies.postValue(repository.getTopRatedMovies().results)
        }
    }

    fun loadLatestMovies() {
        viewModelScope.launch {
            _latestMovies.postValue(repository.getLatestMovies().results)
        }
    }
}