package com.example.assesment.screens.displayImages.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.example.assesment.screens.displayImages.ApplyFilters
import kotlinx.android.synthetic.main.item_image.view.*

class ImageAdapter(private val context: Context):
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private var images: List<DataClass> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false))
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

            }
            itemView.postRequest.setOnClickListener{

            }
            itemView.addFilters.setOnClickListener{
                val intent = Intent(context,ApplyFilters::class.java)
                intent.putExtra("image_url",data.image_url)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itemView.context.startActivity(intent)
            }
        }
    }
}