package com.example.androidwithkotlin.api.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class DefaultInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}