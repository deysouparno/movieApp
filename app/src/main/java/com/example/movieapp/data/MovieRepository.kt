package com.example.movieapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.movieapp.api.MovieApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject  constructor(private val movieApi: MovieApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 80,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MovieSearchPagingSource(movieApi, query)
            }
        ).liveData

    fun getPopularMoviesPaginated() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 80,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PopularMoviePagingSource(movieApi = movieApi)
            }
        ).liveData

    fun getMoviesByGenrePaginated(genres: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 80,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MovieByGenrePagingSource(movieApi = movieApi, genres = genres)
            }
        ).liveData

    fun getTopRatedMoviesPaginated() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 80,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TopRatedMoviePagingSource(movieApi = movieApi)
            }
        ).liveData

    fun getLatestMoviesPaginated() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 80,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                LatestMoviePagingSource(movieApi)
            }
        ).liveData

    suspend fun getPopularMovies() = movieApi.getPopularMovies(page = 1)

    suspend fun getTopRatedMovies() = movieApi.getTopRatedMovies(page = 1)

    suspend fun getLatestMovies() = movieApi.getLatestMovies(page = 1)

    suspend fun getVideos(id: Int) = movieApi.getVideos(id = id)

    suspend fun getMovieDetails(id: Int) = movieApi.getMovieDetails(id = id)

}