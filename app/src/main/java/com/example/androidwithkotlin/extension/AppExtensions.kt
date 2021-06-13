package com.example.androidwithkotlin.extension

import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.androidwithkotlin.view.MainActivity

/**
 * Application context
 */
fun Any.getContext() = MainActivity.instance.applicationContext

/**
 * Activity instance
 */
fun Any.getActivity() = MainActivity.instance

/**
 * Coil svg image loader
 */
val Any.svgLoader
    get() = ImageLoader.Builder(getContext())
        .componentRegistry {
            add(SvgDecoder(getContext()))
        }
        .build()