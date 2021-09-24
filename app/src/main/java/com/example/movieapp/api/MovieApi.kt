package com.example.movieapp.api

import androidx.viewbinding.BuildConfig
import com.example.movieapp.data.MovieDetails
import com.example.movieapp.data.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    companion object {
        const val API_KEY = "f44789d659088ef2a0218abc0eb71eaa"
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"

    ): TmdbResponse

    @GET("discover/movie")
    suspend fun getLatestMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("sort_by") sortBy: String = "release_date.desc",
        @Query("primary_release_year") releaseYear: Int = 2021,
        @Query("page") page: Int,
    ): TmdbResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("sort_by") sortBy: String = "release_date.desc",
        @Query("primary_release_year") releaseYear: Int = 2021,
        @Query("page") page: Int,
        @Query("with_genres") genres: String
    ): TmdbResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int,
    ): TmdbResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int,
    ): TmdbResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = API_KEY

    ): VideoResponse

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieDetails
}