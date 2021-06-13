package com.example.androidwithkotlin.viewmodel

sealed class AppState {
    data class Success<T>(val successData: T) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
