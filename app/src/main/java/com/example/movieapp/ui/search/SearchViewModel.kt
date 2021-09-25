package com.example.movieapp.ui.search

import android.app.DownloadManager
import android.content.Context
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.movieapp.checkForInternet
import com.example.movieapp.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {

    val query = MutableLiveData<String>("black")
    var genres = MutableLiveData<String>("")

    var flag = true

    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    fun checkNetwork(context: Context) {
        viewModelScope.launch {
            _isConnected.postValue(checkForInternet(context = context))
        }
    }

    var searchMovies =  query.switchMap { queryString ->
        if (flag) repository.getPopularMoviesPaginated().cachedIn(viewModelScope)
        else repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    var popularMovies = repository.getPopularMoviesPaginated().cachedIn(viewModelScope)
    var latestMovies = repository.getLatestMoviesPaginated().cachedIn(viewModelScope)
    var topRatedMovies = repository.getTopRatedMoviesPaginated().cachedIn(viewModelScope)
    var moviesByGenre = genres.switchMap {
        repository.getMoviesByGenrePaginated(genres = it).cachedIn(viewModelScope)
    }

}