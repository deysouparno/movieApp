package com.example.movieapp.ui.movie_details

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movieapp.NetworkStatus
import com.example.movieapp.NetworkStatusHelper
import com.example.movieapp.R
import com.example.movieapp.data.Genre
import com.example.movieapp.data.VideoData
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import com.example.movieapp.ui.VideoClickListener
import com.google.android.material.chip.Chip
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailsFragment : Fragment(), VideoClickListener {

    private val binding: FragmentMovieDetailsBinding get() =  _binding!!
    private var _binding: FragmentMovieDetailsBinding? = null

    private val viewModel: MovieDetailsViewModel by viewModels()

//    private val key = "AIzaSyCxLu-t12qvJR79_r3aLj5nxtVRBG6uSn4"

    private var currentVideo = ""

    private val vid = MutableLiveData("")

    private val playerListener = object :
        AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            super.onReady(youTubePlayer)
            try {
                vid.observe(viewLifecycleOwner, {
                    if (it != "") {
                        youTubePlayer.cueVideo(currentVideo, 0F)
                    }
                })
            }
            catch (e: Exception) { }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieDetailsBinding.inflate(inflater)
        val args: MovieDetailsFragmentArgs by navArgs()
        val movie = args.movie

        NetworkStatusHelper(context = requireContext()).observe(viewLifecycleOwner, {
            when(it){
                    NetworkStatus.Available -> {
                        Log.d("network", "details connected")
                    }
                NetworkStatus.Unavailable -> {
                    findNavController().navigate(
                        MovieDetailsFragmentDirections.actionMovieDetailsFragmentToNoNetworkFragment()
                    )
                }
            }
        })


        try {
            viewModel.getVideos(id = movie.id)
            viewModel.getMovieDetails(id = movie.id)
        }
        catch (e: Exception) {

        }


        val videoAdapter = VideosRvAdapter(movieName = movie.title, listener = this)
//        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.detailsToolbar)


        binding.apply {
            videosRv.adapter = videoAdapter
//            detailsToolbar.title = ""
            youtubeFragmentContainer.addYouTubePlayerListener(playerListener)
            Glide.with(moviePoster.context)
                .load("https://image.tmdb.org/t/p/original${movie.poster_path}")
                .into(moviePoster)

        }


        viewModel.movieDetails.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.apply {
                    moviePlot.text = it.overview
                    ratingText.text = "${ratingText.text} ${it.vote_average}"
                    it.genres.forEach { genre ->
                        binding.genreTags.addView(
                            getChip(genre = genre)
                        )
                    }
                }
            }
        })

        viewModel.videos.observe(viewLifecycleOwner, {
            videoAdapter.submitList(it)

            if (it.isNotEmpty()) {
                binding.youtubeFragmentContainer.isVisible = true
                binding.text.text = "More videos"
                getTrailer(it)
            } else {
                binding.text.text = "No videos available"
                binding.youtubeFragmentContainer.isVisible = false
            }
        })

        binding.text.text = movie.title
        return binding.root
    }

    private fun getChip(genre: Genre): Chip {
        val chip = Chip(context)

        chip.apply {
            text = genre.name
            width = 50
            height = 40
            chipBackgroundColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.chip_color))
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setOnClickListener {
                findNavController().navigate(
                    MovieDetailsFragmentDirections
                        .actionMovieDetailsFragmentToSearchFragment(code = "genre ${genre.name} ${genre.id}")
                )
            }

        }

        return chip
    }

    private fun getTrailer(list: List<VideoData>) {

        list.forEach {
            if (it.type == "Trailer") {
                currentVideo = it.key
                vid.value = it.key
                return@forEach
            }
        }


    }

    override fun onClick(video: VideoData) {
        Log.d("youtube", "video clicked")
        currentVideo = video.key
        vid.value = video.key

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}