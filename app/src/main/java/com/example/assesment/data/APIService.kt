package com.example.assesment.data

import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("/image")
    fun getImages(): Call<List<DataClass>>
}