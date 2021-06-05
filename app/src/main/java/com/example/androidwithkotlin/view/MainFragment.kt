package com.example.androidwithkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.databinding.FragmentMainBinding
import com.example.androidwithkotlin.extension.DefaultListAdapter
import com.example.androidwithkotlin.extension.createRecycleViewListAdapter
import com.example.androidwithkotlin.viewmodel.AppState
import com.example.androidwithkotlin.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.androidwithkotlin.extension.showSnackbar
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Country

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    /** Set what a weather will be shown
     * true - world weather
     * false - Russian weather
     * */
    private var isWorldWeather = true

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
                    activity?.let {
                        it.supportFragmentManager
                            .beginTransaction()
                            .add(R.id.container, WeatherDetailsFragment.newInstance(city))
                            .addToBackStack(null)
                            .commit()
                    }
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
    }

    /** Show loading view holder
     * @param isLoading true show loading view holder, false not show
     * */
    private fun setLoading(isLoading: Boolean) {
        binding.mainFragmentLoadingLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.mainFragmentFAB.setOnClickListener {
            it as FloatingActionButton
            if (isWorldWeather) {
                it.setImageResource(R.drawable.ic_russia)
                viewModel.loadCitiesByCountry(Country("Россия"))
            } else {
                it.setImageResource(R.drawable.ic_earth)
                viewModel.loadAllCities()
            }

            isWorldWeather = !isWorldWeather
        }

        initCityRecycleViewAdapter()
        initViewModel()

        viewModel.loadAllCities()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}