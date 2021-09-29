package com.example.assesment.screens.displayImages.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assesment.config.Constants
import com.example.assesment.data.DataClass
import com.example.assesment.data.DataClass2
import com.example.assesment.modules.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagesViewModel : ViewModel() {
    val images = MutableLiveData<List<DataClass>>()
        init {
            getImages()
            getUrlValue()
        }
    private fun getImages(){
        NetworkModule.newInstance().service.getImages().enqueue(object: Callback<List<DataClass>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<DataClass>>, response: Response<List<DataClass>>) {
                Log.i(Constants.LOG_TAG,"onResponse: $response")
                val body = response.body()!!
                images.postValue(body)
            }
            override fun onFailure(call: Call<List<DataClass>>, t: Throwable) {
                Log.i(Constants.LOG_TAG, "onFailure ${t.message.toString()}")
            }
        })
    }
    private fun getUrlValue(){
        NetworkModule.newInstance().service.getURlToPost().enqueue(object : Callback<DataClass2> {
            override fun onResponse(call: Call<DataClass2>, response: Response<DataClass2>) {
                Log.i(Constants.LOG_TAG, "onResponse: $response")
            }

            override fun onFailure(call: Call<DataClass2>, t: Throwable) {
                Log.i(Constants.LOG_TAG, "onFailure ${t.message.toString()}")
            }
        })
    }
}
