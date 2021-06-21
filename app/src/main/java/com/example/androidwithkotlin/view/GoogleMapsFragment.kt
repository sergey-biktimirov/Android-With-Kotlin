package com.example.androidwithkotlin.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.databinding.FragmentGoogleMapsBinding
import com.example.androidwithkotlin.intent.WeatherConstants
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.viewmodel.AppState

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsFragment : BaseFragment() {

    // TODO: 21.06.2021 Перенести в BaseFragment
    private var _binding: FragmentGoogleMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        }

        arguments?.let {
            baseViewModel.getLocationAddress(
                LatLng(
                    it.getDouble(WeatherConstants.Extras.LATITUDE),
                    it.getDouble(WeatherConstants.Extras.LONGITUDE)
                )
            )
        }

        initViewModel()
    }

    private fun initViewModel() {
        baseViewModel.locationAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is AppState.NoAction -> {
                }
                is AppState.Success<*> -> {
                    val city = it.successData as City

                    moveCameraAndAddMarker(
                        latLong = LatLng(city.latitude!!.toDouble(), city.longitude!!.toDouble())
                    )

                    baseViewModel.locationAddressLiveData.value = AppState.NoAction
                }
                is AppState.Error -> {
                    // TODO: 21.06.2021 Error
                }
            }
        }
    }

    private fun moveCameraAndAddMarker(latLong: LatLng) {
        map.addMarker(
            MarkerOptions().position(latLong)
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(latLong))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        binding.apply {
            buttonSearch.setOnClickListener {
                baseViewModel.getLocationAddress(searchAddress.toString())
            }
        }

        mapFragment?.getMapAsync(callback)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.menu_google_maps).isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(lat: Double = 0.0, long: Double = 0.0) = GoogleMapsFragment().apply {
            arguments = Bundle().apply {
                putDouble(WeatherConstants.Extras.LATITUDE, lat)
                putDouble(WeatherConstants.Extras.LONGITUDE, long)
            }
        }
    }
}