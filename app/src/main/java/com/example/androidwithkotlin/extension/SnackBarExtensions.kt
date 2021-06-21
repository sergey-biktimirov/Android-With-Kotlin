package com.example.androidwithkotlin.extension

import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

inline fun View.showSnackbar(
    messageText: String,
    actionText: String,
    crossinline onAction: () -> Unit = {}
) {
    Snackbar
        .make(
            this,
            messageText,
            Snackbar.LENGTH_INDEFINITE
        )
        .setAction(actionText) { onAction() }
        .show()
}