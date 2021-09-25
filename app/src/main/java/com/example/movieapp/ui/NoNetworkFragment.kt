package com.example.movieapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.movieapp.NetworkStatus
import com.example.movieapp.NetworkStatusHelper
import com.example.movieapp.R
import com.example.movieapp.ui.movie_details.MovieDetailsFragmentDirections

class NoNetworkFragment : Fragment() {


    val isConnected = MutableLiveData(false)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        NetworkStatusHelper(context = requireContext()).observe(viewLifecycleOwner, {
            when(it){
                NetworkStatus.Available -> {
                    Log.d("network", "nonetwork connected")
                    findNavController().navigateUp()
                }
                NetworkStatus.Unavailable -> {
                    Log.d("network", "nonetwork disconnected")
                }
            }
        })

        return inflater.inflate(R.layout.fragment_no_network, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun monitorNetwork(connectivityManager: ConnectivityManager) {
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected.postValue(true)
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