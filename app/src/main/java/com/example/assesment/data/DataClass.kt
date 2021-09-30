package com.example.assesment.data

import com.google.gson.annotations.SerializedName

data class DataClass(
    @SerializedName("url") val image_url: String,
    val created: String? = null,
    val updated: String? = null,
)
