package com.example.trp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private val interceptor = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://212.20.47.147:8080")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val userAPI: UserAPI = retrofit.create(UserAPI::class.java)
}