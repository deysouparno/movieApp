package com.example.movieapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.api.MovieApi

class MovieSearchPagingSource(
    private val movieApi: MovieApi,
    private val query: String
): PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        try {
            // Start refresh at page 1 if undefined.
            val position = params.key ?: 1
            val response = movieApi.searchMovie(
                query = query,
                page = position
            )

            val movies = response.results
            return LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.total_pages == position) null else position+1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}



class PopularMoviePagingSource(
    private val movieApi: MovieApi
): PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            // Start refresh at page 1 if undefined.
            val position = params.key ?: 1
            val response = movieApi.getPopularMovies(page = position)

            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.total_pages == position) null else position+1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class LatestMoviePagingSource(
    private val movieApi: MovieApi,
): PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            // Start refresh at page 1 if undefined.
            val position = params.key ?: 1
            val response = movieApi.getLatestMovies(page = position)

            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.total_pages == position) null else position+1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class TopRatedMoviePagingSource(
    private val movieApi: MovieApi,
): PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            // Start refresh at page 1 if undefined.
            val position = params.key ?: 1
            val response = movieApi.getTopRatedMovies(page = position)

            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.total_pages == position) null else position+1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}


class MovieByGenrePagingSource(
    private val movieApi: MovieApi,
    private val genres: String
): PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            // Start refresh at page 1 if undefined.
            val position = params.key ?: 1
            val response = movieApi.getMoviesByGenre(page = position, genres = genres)

            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.total_pages == position) null else position+1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

