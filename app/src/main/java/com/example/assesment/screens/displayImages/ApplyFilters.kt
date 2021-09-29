package com.example.assesment.screens.displayImages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.assesment.R

class ApplyFilters : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_filters)
        initializeViewIDs()

    }
    private fun initializeViewIDs(){
        val imageView = findViewById<ImageView>(R.id.idIVOriginalImage)
        val idOne = findViewById<ImageView>(R.id.idIVOne)
        val idTwo = findViewById<ImageView>(R.id.idIVTwo)
        val idThree = findViewById<ImageView>(R.id.idIVThree)
        val idFour = findViewById<ImageView>(R.id.idIVFour)
        val idFive = findViewById<ImageView>(R.id.idIVFive)
        val idSix = findViewById<ImageView>(R.id.idIVSix)
        val idSeven = findViewById<ImageView>(R.id.idIVSeven)
        val idEight = findViewById<ImageView>(R.id.idIVEight)
        val idNine = findViewById<ImageView>(R.id.idIVNine)
        val idTen = findViewById<ImageView>(R.id.idIVTen)

        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(imageView)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idOne)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idTwo)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idThree)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idFour)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idFive)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idSix)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idSeven)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idEight)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idNine)
        Glide.with(baseContext).load(intent.getStringExtra("image_url")).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(idTen)
    }
}