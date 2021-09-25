package com.example.movieapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.NetworkStatus
import com.example.movieapp.NetworkStatusHelper
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.ui.ClickListener
import com.example.movieapp.ui.movie_details.MovieDetailsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() , ClickListener {

    private val binding: FragmentSearchBinding get() = _binding!!
    private var _binding: FragmentSearchBinding? = null
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(layoutInflater)
        searchAdapter = SearchAdapter(listener = this)

        val args: SearchFragmentArgs by navArgs()
        val code = args.code

        val arr = code.split(" ")

        if (code != "search") {
            binding.textInputLayout.isVisible = false
        }

        NetworkStatusHelper(context = requireContext()).observe(viewLifecycleOwner, {
            when(it){
                NetworkStatus.Available -> {
                    Log.d("network", "search connected")
                }
                NetworkStatus.Unavailable -> {
                    Log.d("network", "search disconnected")
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToNoNetworkFragment()
                    )
                }
            }
        })


        binding.apply {
            searchRv.layoutManager = GridLayoutManager(context, 2)
            searchRv.setHasFixedSize(true)
            searchRv.adapter = searchAdapter
            textInputLayout.setEndIconOnClickListener {

                if (!searchQuery.text.isNullOrEmpty()) {
                    viewModel.flag = false
                    Toast.makeText(context, searchQuery.text.toString(), Toast.LENGTH_SHORT).show()
                    viewModel.query.value = searchQuery.text.toString()
                }
            }
        }

        try {


            when (arr[0]) {
                "top" -> {
                    binding.genreName.isVisible = true
                    binding.genreName.text = "Top Rated Movies"
                    viewModel.topRatedMovies.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                    })
                }

                "popular" -> {
                    binding.genreName.isVisible = true
                    binding.genreName.text = "Popular Movies"
                    viewModel.popularMovies.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                    })
                }

                "latest" -> {
                    binding.genreName.isVisible = true
                    binding.genreName.text = "Latest Movies"
                    viewModel.latestMovies.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                    })
                }

                "genre" -> {
                    viewModel.genres.value = arr.last()
                    binding.genreName.isVisible = true
                    binding.genreName.text = "${arr[1]} Movies"
                    viewModel.moviesByGenre.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                    })
                }

                else -> {
                    viewModel.searchMovies.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                    })
                }
            }
        } catch (E: Exception) {

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(movie: Movie) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(movie = movie)
        )
    }

    override fun viewMore(code: String) = Unit


}