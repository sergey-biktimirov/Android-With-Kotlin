package com.example.androidwithkotlin.extension

import android.widget.ImageView
import coil.load

/**
 * Load svg image with Coil
 * @param url the string url of the target resource
 */
fun ImageView.loadSvgImage(url: String) {
    this.load(
        url,
        svgLoader
    )
}