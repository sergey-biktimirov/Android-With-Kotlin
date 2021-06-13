package com.example.androidwithkotlin.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WeatherBroadcastReceiver(val handleReceive: (context: Context, intent: Intent) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        handleReceive(context, intent)
    }
}