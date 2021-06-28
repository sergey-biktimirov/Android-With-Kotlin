package com.example.androidwithkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.databinding.FragmentMainBinding
import com.example.androidwithkotlin.extension.*
import com.example.androidwithkotlin.viewmodel.AppState
import com.example.androidwithkotlin.viewmodel.MainViewModel
import com.example.androidwithkotlin.constants.WeatherConstants
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Country
import com.google.android.gms.maps.model.LatLng

class MainFragment : BaseFragment() {

    // TODO: 21.06.2021 Перенести в BaseFragment
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: DefaultListAdapter<City>

    /** Initialize recycle view adapter
     * */
    fun initCityRecycleViewAdapter() {
        adapter =
            createRecycleViewListAdapter(
                R.layout.fragment_main_city_recycler_view_item
            ) { view, city ->
                view.setOnClickListener {
                    showDetailsFragment(city)
                }

                view.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                    city.city
            }

        binding.mainFragmentRecyclerView.adapter = adapter
    }

    /** Initialize view model
     * */
    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is AppState.Loading -> {
                    setLoading(true)
                }
                is AppState.Success<*> -> {
                    setLoading(false)

                    adapter.submitList(it.successData as List<City>)
                }
                is AppState.Error -> {
                    setLoading(false)

                    binding.mainFragmentFAB.showSnackbar(
                        messageText = getString(R.string.error),
                        actionText = getString(R.string.reload)
                    ) { viewModel.loadAllCities() }
                }
            }
        })

        viewModel.isWorldWeather.value =
            preferences.getBoolean(WeatherConstants.Preferences.IS_WORLD_WEATHER_KEY, true)
        viewModel.isWorldWeather.observe(viewLifecycleOwner) { isWorldWeather ->
            preferences
                .edit()
                .putBoolean(WeatherConstants.Preferences.IS_WORLD_WEATHER_KEY, isWorldWeather)
                .apply()
            if (isWorldWeather) {
                binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
                viewModel.loadCitiesByCountry(Country("Россия"))
            } else {
                binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
                viewModel.loadAllCities()
            }
        }

        baseViewModel.locationAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is AppState.NoAction -> {}
                is AppState.Loading -> {
                    setLoading(true)
                }
                is AppState.Success<*> -> {
                    setLoading(false)

                    val city = it.successData as City

                    showAlertDialog(
                        title = getString(R.string.show_weather_at_current_location),
                        message = getString(R.string.your_current_address, city.city),
                        positiveButtonText = getString(R.string.show)
                    ) { _, _ ->
                        showDetailsFragment(city)
                    }

                    baseViewModel.locationAddressLiveData.value = AppState.NoAction
                }
                is AppState.Error -> {
                    setLoading(false)

                    binding.mainFragmentFABLocation.showSnackbar(
                        messageText = getString(R.string.error),
                        actionText = getString(R.string.close)
                    )
                }
            }
        }

        viewModel.loadAllCities()
    }

    /** Show loading view holder
     * @param isLoading true show loading view holder, false not show
     * */
    private fun setLoading(isLoading: Boolean) {
        binding.mainFragmentLoadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                showHistoryFragment()
                true
            }
            R.id.menu_google_maps -> {
                showGoogleMaps()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkGpsPermission()

        binding.mainFragmentFAB.setOnClickListener {
            viewModel.isWorldWeather.value = !viewModel.isWorldWeather.value!!
        }

        binding.mainFragmentFABLocation.setOnClickListener {
            // TODO: 21.06.2021 Добавить анимацию определения местоположения
            getLocation {location ->
                baseViewModel.getLocationAddress(
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                )
            }
        }

        initCityRecycleViewAdapter()
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}