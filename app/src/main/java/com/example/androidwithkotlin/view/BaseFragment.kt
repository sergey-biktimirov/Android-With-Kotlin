package com.example.androidwithkotlin.view

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.intent.WeatherConstants
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.viewmodel.BaseViewModel
import com.example.androidwithkotlin.viewmodel.BaseViewModelFactory
import com.google.android.gms.maps.model.LatLng

open class BaseFragment : Fragment() {

    val baseViewModel by viewModels<BaseViewModel> {
        BaseViewModelFactory()
    }

    private val requestPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private val _permissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                when (it.key) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        if (!it.value) {
                            showAlertDialog(
                                title = getString(R.string.dialog_rationale_title),
                                message = getString(R.string.dialog_rationale_meaasge),
                                showPositiveButton = false
                            )
                        }
                    }
                }
            }

        }

    fun showGoogleMaps(lat: Double = 0.0, long: Double = 0.0) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(
                R.id.container, GoogleMapsFragment.newInstance(
                    lat = lat,
                    long = long
                )
            )
            .addToBackStack("")
            .commitAllowingStateLoss()
    }

    /** Запрос текущего местоположения
     * */
    fun getLocation(onLocationReceived: (location: Location) -> Unit = {}) {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val provider = locationManager.getBestProvider(
                    Criteria().apply {
                        accuracy = Criteria.ACCURACY_COARSE
                    },
                    true
                )

                provider?.let {
                    // Получение геоположения через каждые 60 секунд или каждые 100 метров
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        WeatherConstants.LocationManager.REFRESH_PERIOD,
                        WeatherConstants.LocationManager.MINIMAL_DISTANCE
                    ) { location ->
                        onLocationReceived(location)
                    }
                }
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (location == null) {
                    showAlertDialog(
                        title = getString(R.string.location_manager_gps_disabled_title),
                        message = getString(R.string.location_manager_gps_disabled_message),
                        showPositiveButton = false
                    )
                } else {
                    baseViewModel.getLocationAddress(
                        latLng = LatLng(
                            location.latitude,
                            location.longitude
                        )
                    )

                    showAlertDialog(
                        title = getString(R.string.dialog_rationale_title),
                        message = getString(R.string.dialog_rationale_meaasge),
                        showPositiveButton = false
                    )
                }
            }

        } else {
            showAlertDialog(
                title = getString(R.string.dialog_rationale_title),
                message = getString(R.string.dialog_rationale_meaasge),
                showPositiveButton = false
            ) { _, _ ->
                requestPermissions()
            }
        }
    }

    /** Запрос разрешений
     * */
    private fun requestPermissions() {
        _permissionRequest.launch(requestPermissions)
    }

    /** Проверка разрешения на досдуп к GPS
     * */
    fun checkGpsPermission() {
        context?.let {
            when {
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showAlertDialog(
                        title = getString(R.string.dialog_rationale_title),
                        message = getString(R.string.dialog_rationale_meaasge),
                    ) { _, _ ->
                        requestPermissions()
                    }
                }
                else -> {
                    requestPermissions()
                }
            }
        }
    }

    fun showDetailsFragment(city: City) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container,
                WeatherDetailsFragment.newInstance(city)
            )
            .addToBackStack(null)
            .commit()
    }

    fun showHistoryFragment() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container,
                WeatherViewHistoryFragment.newInstance()
            )
            .addToBackStack(null)
            .commit()
    }

    fun showAlertDialog(
        title: String,
        message: String,
        negativeButtonText: String = getString(R.string.close),
        positiveButtonText: String = getString(R.string.grant_access),
        showPositiveButton: Boolean = true,
        onNegativeCallback: (dialog: DialogInterface, which: Int) -> Unit = { dialog, _ -> dialog.dismiss() },
        onPositiveCallback: (dialog: DialogInterface, which: Int) -> Unit = { dialog, _ -> dialog.dismiss() }
    ) {
        val alert = AlertDialog.Builder(requireContext())
        alert
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negativeButtonText) { dialog, which ->
                onNegativeCallback(
                    dialog,
                    which
                )
            }

        if (showPositiveButton) alert.setPositiveButton(positiveButtonText) { dialog, which ->
            onPositiveCallback(dialog, which)
        }

        alert.create().show()
    }
}