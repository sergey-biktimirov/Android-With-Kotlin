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
import com.example.androidwithkotlin.model.Weather
import com.example.androidwithkotlin.viewmodel.AppState
import com.example.androidwithkotlin.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


/** Detail weather bundle key
 * */
const val WEATHER_DETAIL_KEY = "WEATHER_DETAIL"
/** Detailing weather by city
 */
class WeatherDetailsFragment : Fragment() {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            val weather = it.getParcelable<Weather>(WEATHER_DETAIL_KEY)
            if (weather != null) {
                setData(weather)
            }
        }
    }

    private fun setData(weatherData: Weather) {
        binding.apply {
            cityName.text = weatherData.city.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weatherData.city.latitude.toString(),
                weatherData.city.longitude.toString()
            )
            temperatureValue.text = weatherData.temperature.toString()
            feelsLikeValue.text = weatherData.feelsLike.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(weather: Weather) = WeatherDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(WEATHER_DETAIL_KEY, weather)
            }
        }
    }
}