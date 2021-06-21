package com.example.androidwithkotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.databinding.FragmentWeatherViewHistoryBinding
import com.example.androidwithkotlin.db.room.entity.WeatherViewHistoryEntity
import com.example.androidwithkotlin.extension.*
import com.example.androidwithkotlin.intent.WeatherConstants
import com.example.androidwithkotlin.repository.RoomWeatherViewHistoryRepository
import com.example.androidwithkotlin.viewmodel.WeatherViewHistoryViewModel
import com.example.androidwithkotlin.viewmodel.WeatherViewHistoryViewModelFactory

/**
 * This fragment shows the history of viewing the weather forecast
 */
class WeatherViewHistoryFragment : Fragment() {

    // TODO: 21.06.2021 Перенести в BaseFragment
    private var _binding: FragmentWeatherViewHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewHistoryViewModel by viewModels {
        WeatherViewHistoryViewModelFactory(RoomWeatherViewHistoryRepository())
    }

    private lateinit var adapter: DefaultListAdapter<WeatherViewHistoryEntity>

    private fun initRecyclerView() {
        adapter =
            createRecycleViewListAdapter(
                R.layout.weather_history_view_recyclerview_item
            ) { view, weatherViewHistory ->
                view.findViewById<TextView>(R.id.date).text =
                    weatherViewHistory.addDate.formatDateTimeToContextDateFormat()
                view.findViewById<TextView>(R.id.temperature).text =
                    getString(R.string.temperature_t, weatherViewHistory.temperature)
                view.findViewById<TextView>(R.id.feelsLike).text =
                    getString(R.string.feels_like_t, weatherViewHistory.feelsLike)

                val citiView = view.findViewById<TextView>(R.id.city).apply {
                    text = weatherViewHistory.city
                }

                arguments?.let {
                    val cityName = it.getString(WeatherConstants.Extras.WEATHER_CITY_NAME)

                    if (cityName.isNullOrEmpty()) {
                        citiView.show()
                    } else {
                        citiView.hide()
                    }
                }
            }

        binding.recyclerview.adapter = adapter
    }

    private fun initViewModel() {
        arguments?.let {
            val cityName = it.getString(WeatherConstants.Extras.WEATHER_CITY_NAME)

            if (cityName.isNullOrEmpty()) {
                viewModel.getAll().observe(viewLifecycleOwner) { all ->
                    adapter.submitList(all)
                }
            } else {
                viewModel.getByCityName(cityName).observe(viewLifecycleOwner) { byCityName ->
                    adapter.submitList(byCityName)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherViewHistoryBinding.inflate(inflater, container, false)
        initRecyclerView()
        initViewModel()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(cityName: String? = null) =
            WeatherViewHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(WeatherConstants.Extras.WEATHER_CITY_NAME, cityName)
                }
            }
    }
}