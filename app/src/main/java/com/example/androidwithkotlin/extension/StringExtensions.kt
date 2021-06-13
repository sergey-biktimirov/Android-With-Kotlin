package com.example.androidwithkotlin.extension

import android.net.Uri

/**
 * Creates a Uri which parses the given encoded URI string.
 */
fun String.parseUri(): Uri = Uri.parse(this)