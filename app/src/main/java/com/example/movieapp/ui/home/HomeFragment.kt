package com.example.movieapp.ui.home

import android.net.*
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.movieapp.NetworkStatus
import com.example.movieapp.NetworkStatusHelper
import com.example.movieapp.R
import com.example.movieapp.data.Movie
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.ui.ClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception


@AndroidEntryPoint
class HomeFragment : Fragment(), ClickListener {

    private val binding: FragmentHomeBinding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    private lateinit var popularMovies: MutableList<Movie>
    private lateinit var topRatedMovies: MutableList<Movie>
    private lateinit var latestMovies: MutableList<Movie>

    private lateinit var popularMovieAdapter: HomeAdapter
    private lateinit var topRatedMovieAdapter: HomeAdapter
    private lateinit var latestMovieAdapter: HomeAdapter
    private lateinit var explodeAnimation: Animation

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(layoutInflater)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.homeToolbar)
        setHasOptionsMenu(true)
        setUpRecyclerViews()

        explodeAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.explode_animation).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        NetworkStatusHelper(context = requireContext()).observe(viewLifecycleOwner, {
            Log.d("network", "home observed")
            when(it){
                NetworkStatus.Available -> {
                    Log.d("network", "home connected")
                    loadData()
                }
                else -> {
                    Log.d("network", "home disconnected")
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToNoNetworkFragment()
                    )
                }
            }
        })



        viewModel.popularMovies.observe(viewLifecycleOwner, {
            popularMovieAdapter.submitList(it)
            if (it.isNotEmpty()) {
                binding.trendingMovieShimmer.isVisible = false
            }
        })

        viewModel.topRatedMovies.observe(viewLifecycleOwner, {
            topRatedMovieAdapter.submitList(it)
            if (it.isNotEmpty()) {
                binding.topMovieShimmer.isVisible = false
            }
        })

        viewModel.latestMovies.observe(viewLifecycleOwner, {
            latestMovieAdapter.submitList(it)
            if (it.isNotEmpty()) {
                binding.latestMovieShimmer.isVisible = false
            }
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

    }

    private fun loadData() {
        try {
            viewModel.loadPopularMovies()
            viewModel.loadTopRatedMovies()
            viewModel.loadLatestMovies()
        }
        catch (e: Exception) {

        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.home_search_menu_item -> {
//                binding.explodeView.visibility = View.VISIBLE
//                binding.explodeView.startAnimation(explodeAnimation) {
//                    binding.explodedView.isVisible = true
//                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
//                }
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(movie: Movie, movieImg: ImageView) {
        if (viewModel.popularMovies.value == null) return
        movieImg.transitionName = movie.title
        val extras = FragmentNavigatorExtras(movieImg to "big_poster")
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movie),
            extras
        )
    }

    override fun viewMore(code: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(code = code)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun monitorNetwork(connectivityManager: ConnectivityManager) {
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {

            }

            override fun onLost(network: Network) = Unit

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) = Unit

            override fun onLinkPropertiesChanged(
                network: Network,
                linkProperties: LinkProperties
            ) = Unit
        })
    }

}

fun View.startAnimation(animation: Animation, onEnd: () -> Unit) {
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(animation: Animation?) = Unit

    })

    this.startAnimation(animation)
}
