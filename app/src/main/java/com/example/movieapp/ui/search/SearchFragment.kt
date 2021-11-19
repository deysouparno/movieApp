package com.example.movieapp.ui.search

import android.app.Activity
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.NetworkStatus
import com.example.movieapp.NetworkStatusHelper
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.ui.ClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), ClickListener {

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

        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        val arr = code.split(" ")

        if (code != "search") {
            binding.textInputLayout.isVisible = false
        }

        NetworkStatusHelper(context = requireContext()).observe(viewLifecycleOwner, {
            when (it) {
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
                search()
            }


            searchQuery.setOnEditorActionListener { v, actionId, event ->
                Log.d("search", "key pressed")

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        }

        try {
            when (arr[0]) {
                "top" -> {
                    binding.genreName.isVisible = true
                    binding.genreName.text = "Top Rated Movies"
                    viewModel.topRatedMovies.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                        if (searchAdapter.itemCount > 0) {
                            binding.searchShimmer.isVisible = false
                        }
                    })
                }

                "popular" -> {
                    binding.genreName.isVisible = true
                    binding.genreName.text = "Popular Movies"

                    viewModel.popularMovies.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                        if (searchAdapter.itemCount > 0) {
                            binding.searchShimmer.isVisible = false
                        }
                    })
                }

                "latest" -> {
                    binding.genreName.isVisible = true
                    binding.genreName.text = "Latest Movies"
                    binding.searchShimmer.isVisible = false
                    viewModel.latestMovies.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                        if (searchAdapter.itemCount > 0) {
                            binding.searchShimmer.isVisible = false
                        }
                    })
                }

                "genre" -> {
                    viewModel.genres.value = arr.last()
                    binding.genreName.isVisible = true
                    binding.genreName.text = "${arr[1]} Movies"
                    binding.searchShimmer.isVisible = false
                    viewModel.moviesByGenre.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                        if (searchAdapter.itemCount > 0) {
                            binding.searchShimmer.isVisible = false
                        }

                    })
                }

                else -> {
                    viewModel.searchMovies.observe(viewLifecycleOwner, {
                        searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                        binding.searchShimmer.isVisible = false
                    })

                }
            }
        } catch (E: Exception) {

        }

        return binding.root
    }


    private fun search() {
        binding.apply {
            if (!searchQuery.text.isNullOrEmpty()) {
                viewModel.flag = false
                binding.searchShimmer.isVisible = true
                viewModel.query.value = searchQuery.text.toString()
                Log.d("search", "search called, adapter itemcount: ${searchAdapter.itemCount > 0}")

            }
        }
        hideKeyboard()
    }

    private fun hideKeyboard() {
        binding.searchQuery.apply {
            (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.windowToken, 0)
            clearFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(movie: Movie, movieImg: ImageView) {
        movieImg.transitionName = movie.title
        val extras = FragmentNavigatorExtras(movieImg to "big_poster")
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToMovieDetailsFragment(movie = movie),
            extras
        )
    }

    override fun viewMore(code: String) = Unit


}