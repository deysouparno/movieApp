package com.example.movieapp.ui.home

import android.content.Context
import android.net.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.checkForInternet
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.ui.ClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), ClickListener {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var popularMovies: MutableList<Movie>
    private lateinit var topRatedMovies: MutableList<Movie>
    private lateinit var latestMovies: MutableList<Movie>

    private lateinit var popularMovieAdapter: HomeAdapter
    private lateinit var topRatedMovieAdapter: HomeAdapter
    private lateinit var latestMovieAdapter: HomeAdapter

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.homeToolbar)
        setHasOptionsMenu(true)

        if (checkForInternet(context = requireContext())) {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNoNetworkFragment()
            )
        }

        setUpRecyclerViews()

        viewModel.popularMovies.observe(viewLifecycleOwner, {
            popularMovieAdapter.submitList(it)
        })

        viewModel.topRatedMovies.observe(viewLifecycleOwner, {
            topRatedMovieAdapter.submitList(it)
        })

        viewModel.latestMovies.observe(viewLifecycleOwner, {
            latestMovieAdapter.submitList(it)
        })




        return binding.root
    }

    private fun setUpRecyclerViews() {
        popularMovies = mutableListOf()
        topRatedMovies = mutableListOf()
        latestMovies = mutableListOf()

        popularMovieAdapter = HomeAdapter(this, code = "popular")
        topRatedMovieAdapter = HomeAdapter(this, code = "top")
        latestMovieAdapter = HomeAdapter(this, code = "latest")

        binding.apply {
            topMovieRv.adapter = popularMovieAdapter
            trendingMovieRv.adapter = topRatedMovieAdapter
            latestMovieRv.adapter = latestMovieAdapter
        }

        viewModel.loadPopularMovies()
        viewModel.loadTopRatedMovies()
        viewModel.loadLatestMovies()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.home_search_menu_item -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(movie: Movie) {
        if (viewModel.popularMovies.value == null) return
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movie)
        )
    }

    override fun viewMore(code: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(code = code)
        )
    }

}
