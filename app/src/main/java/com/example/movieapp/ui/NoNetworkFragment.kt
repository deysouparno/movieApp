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
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R

class NoNetworkFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            monitorNetwork(connectivityManager = connectivityManager)

        return inflater.inflate(R.layout.fragment_no_network, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun monitorNetwork(connectivityManager: ConnectivityManager) {
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                findNavController().navigateUp()
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