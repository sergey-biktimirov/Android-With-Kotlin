package com.example.androidwithkotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.databinding.FragmentWeatherDetailsBinding
import com.example.androidwithkotlin.extension.hide
import com.example.androidwithkotlin.extension.show
import com.example.androidwithkotlin.extension.showSnackbar
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Weather
import com.example.androidwithkotlin.viewmodel.AppState
import com.example.androidwithkotlin.viewmodel.WeatherDetailsViewModel
import kotlinx.android.synthetic.main.main_fragment.*


/** Detail weather bundle key
 * */
const val WEATHER_DETAIL_KEY = "WEATHER_DETAIL"

/** Detailing weather by city
 */
class WeatherDetailsFragment : Fragment() {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherDetailsViewModel by lazy {
        ViewModelProvider(this).get(WeatherDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val city = it.getParcelable<City>(WEATHER_DETAIL_KEY)
            if (city != null) {
                setCityData(city)

                viewModel.loadWeatherByCity(city)
            }
        }

        viewModel.weatherState.observe(viewLifecycleOwner, Observer {
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
                    ) {viewModel.reloadDataByCity()}
                }
            }
        })
    }

    /** Show loading view holder
     * @param isLoading true show loading view holder, false not show
     * */
    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading){
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
        }
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