package com.example.androidwithkotlin.extension

import com.example.androidwithkotlin.view.MainActivity

fun Any.getStringResource(resId: Int) =
    MainActivity.instance.getString(resId)

fun Any.getStringResource(resId: Int, vararg formatArgs: Any) =
    MainActivity.instance.getString(resId, formatArgs)