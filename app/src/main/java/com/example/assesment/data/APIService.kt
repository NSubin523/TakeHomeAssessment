package com.example.assesment.data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @GET("/image")
    fun getImages(): Call<List<DataClass>>

    @GET("/upload")
    fun getURlToPost(): Call<DataClass2>

    @POST()
    fun sendImages(data: DataClass): Call<DataClass>
}