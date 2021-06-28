package com.example.androidwithkotlin.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.androidwithkotlin.BuildConfig
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.broadcast_receiver.WeatherBroadcastReceiver
import com.example.androidwithkotlin.databinding.FragmentWeatherDetailsBinding
import com.example.androidwithkotlin.extension.*
import com.example.androidwithkotlin.constants.WeatherConstants
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Weather
import com.example.androidwithkotlin.repository.RoomWeatherViewHistoryRepository
import com.example.androidwithkotlin.repository.YandexWeatherRepository
import com.example.androidwithkotlin.service.WeatherLoaderService
import com.example.androidwithkotlin.viewmodel.*
import kotlinx.android.synthetic.main.main_fragment.*


/** Detail weather bundle key
 * */
const val WEATHER_DETAIL_KEY = "WEATHER_DETAIL"

/** Detailing weather by city
 */
class WeatherDetailsFragment : BaseFragment() {

    // TODO: 21.06.2021 Перенести в BaseFragment
    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!

    private val weatherDetailsViewModel: WeatherDetailsViewModel by viewModels {
        WeatherDetailsViewModelFactory(
            YandexWeatherRepository(),
            RoomWeatherViewHistoryRepository()
        )
    }
    private lateinit var weatherBroadcastReceiver: WeatherBroadcastReceiver

    /** Show loading view holder
     * @param isLoading true show loading view holder, false not show
     * */
    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                loadingLayout.show()
            } else {
                loadingLayout.hide()
            }
        }
    }

    private fun setCityData(city: City) {
        cityName.text = city.city
        cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.latitude.toString(),
            city.longitude.toString()
        )
    }

    private fun setWeatherData(weatherData: Weather) {
        binding.apply {
            temperatureValue.text = weatherData.temperature.toString()
            feelsLikeValue.text = weatherData.feelsLike.toString()
            weatherIconImageView
                .loadSvgImage(BuildConfig.YANDEX_WEATHER_ICON_URL.format(weatherData.icon))
        }
    }

    private fun setWeatherData(temperature: Int, feelsLike: Int, icon: String) {
        binding.apply {
            temperatureValue.text = temperature.toString()
            feelsLikeValue.text = feelsLike.toString()
            weatherIconImageView.loadSvgImage(BuildConfig.YANDEX_WEATHER_ICON_URL.format(icon))
        }
    }

    private fun registerBroadcastReceivers() {
        weatherBroadcastReceiver = WeatherBroadcastReceiver { context, intent ->
            Log.d(TAG(), "intent.action = ${intent.action}")

            when (intent.action) {
                WeatherConstants.Action.WEATHER_BY_COORDINATES_LOADING -> {
                    val isLoading =
                        intent.getBooleanExtra(
                            WeatherConstants.Extras.WEATHER_LOADING,
                            false
                        )
                    setLoading(isLoading)

                    Log.d(TAG(), "weather isLoading = $isLoading")
                }
                WeatherConstants.Action.WEATHER_BY_COORDINATES_LOADED -> {
                    val temperature =
                        intent.getIntExtra(WeatherConstants.Extras.TEMPERATURE, 0)
                    val fellsLike =
                        intent.getIntExtra(WeatherConstants.Extras.FEELS_LIKE, 0)
                    val icon =
                        intent.getStringExtra(WeatherConstants.Extras.ICON) ?: ""

                    setWeatherData(temperature, fellsLike, icon)

                    Log.d(
                        TAG(),
                        "weather temperature = $temperature, " +
                                "feelsLike = $fellsLike, " +
                                "icon = $icon"
                    )
                }
            }
        }.apply {

        }

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(
                weatherBroadcastReceiver,
                IntentFilter().apply {
                    this.addAction(WeatherConstants.Action.WEATHER_BY_COORDINATES_LOADED)
                    this.addAction(WeatherConstants.Action.WEATHER_BY_COORDINATES_LOADING)
                }
            )
    }

    private fun unregisterBroadcastReceivers() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(weatherBroadcastReceiver)
    }

    private fun startWeatherLoaderService() {
        Log.d(TAG(), "city = ${weatherDetailsViewModel.city}")
        requireContext().apply {
            this.startService(
                Intent(this, WeatherLoaderService::class.java)
                    .apply {
                        this.action = WeatherConstants.Action.LOAD_WEATHER_BY_COORDINATES
                        this.putExtra(
                            WeatherConstants.Extras.LATITUDE,
                            weatherDetailsViewModel.city!!.latitude
                        )
                        this.putExtra(
                            WeatherConstants.Extras.LONGITUDE,
                            weatherDetailsViewModel.city!!.longitude
                        )
                    }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.container,
                        WeatherViewHistoryFragment.newInstance(weatherDetailsViewModel.city?.city)
                    )
                    .addToBackStack(null)
                    .commit()
                true
            }
            R.id.menu_google_maps -> {
                showGoogleMaps(
                    weatherDetailsViewModel.city?.latitude ?: 0.0,
                    weatherDetailsViewModel.city?.longitude ?: 0.0
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBroadcastReceivers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO Works no as expected, need something like a swipe down
        binding.mainView.setOnTouchListener { v, event ->
            val action = MotionEventCompat.getActionMasked(event)
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    startWeatherLoaderService()
                }
            }
            true
        }

        arguments?.let {
            val city = it.getParcelable<City>(WEATHER_DETAIL_KEY)
            if (city != null) {
                setCityData(city)

                weatherDetailsViewModel.loadWeatherByCity(city)
            }
        }

        weatherDetailsViewModel.weatherState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is AppState.Loading -> {
                    setLoading(true)
                }
                is AppState.Success<*> -> {
                    setLoading(false)
                    setWeatherData(it.successData as Weather)
                }
                is AppState.Error -> {
                    setLoading(false)
                    val errorState = it as AppState.Error

                    binding.loadingLayout.showSnackbar(
                        messageText = errorState.error.localizedMessage ?: "Unknown error occurred",
                        actionText = getString(R.string.reload)
                    ) { weatherDetailsViewModel.reloadDataByCity() }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcastReceivers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(city: City) = WeatherDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(WEATHER_DETAIL_KEY, city)
            }
        }
    }
}