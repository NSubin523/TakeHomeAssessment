package com.example.assesment.modules

import com.example.assesment.config.Constants
import com.example.assesment.data.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule private constructor() {

    private val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service: APIService = retrofit.create(APIService::class.java)

    companion object {
        private lateinit var instance: NetworkModule

        fun newInstance(): NetworkModule {
            if (!::instance.isInitialized) {
                instance = NetworkModule()
            }
            return instance
        }
    }
}