package com.example.assesment.screens.displayImages.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.assesment.R
import com.example.assesment.data.DataClass
import kotlinx.android.synthetic.main.item_image.view.*

class ImageAdapter(
    private val context: Context,
    val onDownload: (url: String) -> Unit,
    val onUpload: (id: String) -> Unit,
    val onFilter: (url: String) -> Unit
) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var images: List<DataClass> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_image,
                parent, false
            ), onDownload, onUpload, onFilter
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    fun setImages(images: List<DataClass>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(
        itemView: View, val onDownload: (url: String) -> Unit,
        val onUpload: (id: String) -> Unit,
        val onFilter: (url: String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(data: DataClass) {
            itemView.createdAtID.text = "Created At: ${data.created}"
            itemView.updatedAtID.text = "Updated At: ${data.updated}"
            Glide.with(context).load(data.image_url).apply(
                RequestOptions().transforms(
                    CenterCrop(), RoundedCorners(20)
                )
            ).into(itemView.imageView)

            itemView.downloadButton.setOnClickListener {
                onDownload(data.image_url)
            }
            itemView.postRequest.setOnClickListener {
                onUpload(data.image_url)
            }
            itemView.addFilters.setOnClickListener {
                onFilter(data.image_url)
            }
        }
    }
}