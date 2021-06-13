package com.example.androidwithkotlin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidwithkotlin.R

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var instance: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instance = this

        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
