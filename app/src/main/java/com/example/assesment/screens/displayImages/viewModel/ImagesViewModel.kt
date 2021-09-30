package com.example.assesment.screens.displayImages.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assesment.config.Constants
import com.example.assesment.core.ResultState
import com.example.assesment.data.DataClass
import com.example.assesment.data.DataClass2
import com.example.assesment.modules.NetworkModule
import com.example.assesment.utils.RealPathUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ImagesViewModel : ViewModel() {
    val images = MutableLiveData<List<DataClass>>()
    val uploadState = MutableLiveData<ResultState>()

    init {
        getImages()
    }

    private fun getImages() {
        NetworkModule.newInstance().service.getImages().enqueue(object : Callback<List<DataClass>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<List<DataClass>>,
                response: Response<List<DataClass>>
            ) {
                Log.i(Constants.LOG_TAG, "onResponse: $response")
                val body = response.body()!!
                images.postValue(body)
            }

            override fun onFailure(call: Call<List<DataClass>>, t: Throwable) {
                Log.i(Constants.LOG_TAG, "onFailure ${t.message.toString()}")
            }
        })
    }

    fun uploadImage(url: Uri, context: Context, originalId: String) {

        uploadState.postValue(ResultState.Loading)

        val file = File(RealPathUtils.getRealPathFromURI_API19(context, url))
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)

        val original: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), originalId)
        val appid: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), "neosubin729@gmail.com")

        NetworkModule.newInstance().service.getURlToPost().enqueue(object : Callback<DataClass2> {
            override fun onResponse(call: Call<DataClass2>, response: Response<DataClass2>) {
                Log.i(Constants.LOG_TAG, "onResponse: $response")
                NetworkModule.newInstance().service.sendImages(
                    response.body()!!.post_url,
                    appid,
                    original,
                    body
                )
                    .enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            uploadState.postValue(ResultState.Success)
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            uploadState.postValue(ResultState.Error("Error uploading image"))
                        }

                    })
            }

            override fun onFailure(call: Call<DataClass2>, t: Throwable) {
                Log.i(Constants.LOG_TAG, "onFailure ${t.message.toString()}")
                uploadState.postValue(ResultState.Error("Error getting upload url"))
            }
        })
    }
}
