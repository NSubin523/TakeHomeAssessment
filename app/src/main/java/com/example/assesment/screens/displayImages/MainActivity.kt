package com.example.assesment.screens.displayImages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assesment.R
import com.example.assesment.screens.displayImages.adapter.ImageAdapter
import com.example.assesment.screens.displayImages.viewModel.ImagesViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val model: ImagesViewModel by viewModels()
    private val adapter: ImageAdapter by lazy {
        ImageAdapter(baseContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageRecyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        imageRecyclerView.layoutManager = linearLayoutManager
        imageRecyclerView.adapter = adapter
        model.images.observe(this,{
            adapter.setImages(it)
        })
    }
}