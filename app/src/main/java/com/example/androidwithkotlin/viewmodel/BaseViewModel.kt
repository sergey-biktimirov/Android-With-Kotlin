package com.example.androidwithkotlin.viewmodel

import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.*
import com.example.androidwithkotlin.exception.viewmodel.AddressNotFoundException
import com.example.androidwithkotlin.exception.viewmodel.UnknownViewModelClassException
import com.example.androidwithkotlin.extension.getApplicationContext
import com.example.androidwithkotlin.model.City
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.lang.Exception

class BaseViewModel : ViewModel() {

    val locationAddressLiveData = MutableLiveData<AppState>()

    fun getLocationAddress(latLng: LatLng) {
        val geoCoder = Geocoder(getApplicationContext())

        this.locationAddressLiveData.value = AppState.Loading

        viewModelScope.launch {
            try {
                val addresses = geoCoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
                ).firstOrNull()?.getAddressLine(0) ?: ""

                this@BaseViewModel.locationAddressLiveData.value = AppState.Success(
                    City(
                        city = addresses,
                        latitude = latLng.latitude,
                        longitude = latLng.longitude
                    )
                )
            } catch (e: Exception) {
                this@BaseViewModel.locationAddressLiveData.value = AppState.Error(e)
            }
        }
    }

    fun getLocationAddress(address: String) {
        val geoCoder = Geocoder(getApplicationContext())

        this.locationAddressLiveData.value = AppState.Loading

        viewModelScope.launch {
            try {
                val addresses = geoCoder.getFromLocationName(
                    address,
                    1
                ).firstOrNull()

                if (address != null) {
                    this@BaseViewModel.locationAddressLiveData.value = AppState.Success(
                        City(
                            city = addresses?.getAddressLine(0) ?: "",
                            latitude = addresses?.latitude,
                            longitude = addresses?.longitude
                        )
                    )
                } else {
                    this@BaseViewModel.locationAddressLiveData.value =
                        AppState.Error(AddressNotFoundException())
                }
            } catch (e: Exception) {
                this@BaseViewModel.locationAddressLiveData.value = AppState.Error(e)
            }
        }
    }
}

class BaseViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseViewModel::class.java)) {
            return BaseViewModel() as T
        } else {
            throw UnknownViewModelClassException()
        }
    }
}