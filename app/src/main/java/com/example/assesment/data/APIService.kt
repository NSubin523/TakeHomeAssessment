package com.example.assesment.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("/image")
    fun getImages(): Call<List<DataClass>>

    @GET("/upload")
    fun getURlToPost(): Call<DataClass2>

    @Multipart
    @POST()
    fun sendImages(
        @Url url: String,
        @Part("appid") appid: RequestBody,
        @Part("original") original: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<Any>
}