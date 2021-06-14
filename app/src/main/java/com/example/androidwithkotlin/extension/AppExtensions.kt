package com.example.androidwithkotlin.extension

import android.content.Context
import android.content.SharedPreferences
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.androidwithkotlin.App
import com.example.androidwithkotlin.view.MainActivity

/**
 * Application context
 */
fun Any.getApplicationContext(): Context = MainActivity.instance.applicationContext

/**
 * Activity instance
 */
fun Any.getActivity() = MainActivity.instance

/**
 * Coil svg image loader
 */
val Any.svgLoader
    get() = ImageLoader.Builder(getApplicationContext())
        .componentRegistry {
            add(SvgDecoder(getApplicationContext()))
        }
        .build()

/**
 * Private app preferences
 */
val Any.preferences: SharedPreferences
    get() = MainActivity.instance.getPreferences(Context.MODE_PRIVATE)

val Any.roomDataBase
    get() = App.instance.roomDataBase