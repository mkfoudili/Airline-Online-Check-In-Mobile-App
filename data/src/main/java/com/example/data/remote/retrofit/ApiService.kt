package com.example.data.remote.retrofit

import com.example.data.remote.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Endpoint {

}

object ApiService {
    val api: Endpoint by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Endpoint::class.java)
    }
}