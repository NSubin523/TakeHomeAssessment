package com.example.assesment.screens.displayImages.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.assesment.R
import com.example.assesment.data.DataClass
import com.example.assesment.screens.displayImages.ApplyFilters
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

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
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Download").setMessage("Do you want to download this image?")
                    .setNegativeButton("No",null).setPositiveButton("Yes") { dialogInterface, i ->
                        saveImage(data.image_url)
                    }
                val alert = builder.create()
                alert.show()
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

        var msg: String? = ""
        var lastMsg = ""

        private fun saveImage(url: String){
            val directory = File(Environment.DIRECTORY_PICTURES)

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val downloadManager = Context.DOWNLOAD_SERVICE as DownloadManager

            val downloadUri = Uri.parse(url)

            val request = DownloadManager.Request(downloadUri).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(url.substring(url.lastIndexOf("/") + 1))
                    .setDescription("")
                    .setDestinationInExternalPublicDir(
                        directory.toString(),
                        url.substring(url.lastIndexOf("/") + 1)
                    )
            }
            val downloadId = downloadManager.enqueue(request)
            val query = DownloadManager.Query().setFilterById(downloadId)
            Thread(Runnable {
                var downloading = true
                while (downloading) {
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                    }
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    msg = statusMessage(url, directory, status)
                    if (msg != lastMsg) {
                        this.run {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                        lastMsg = msg ?: ""
                    }
                    cursor.close()
                }
            }).start()
        }
        private fun statusMessage(url: String, directory: File, status: Int): String? {
            var msg = ""
            msg = when (status) {
                DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
                DownloadManager.STATUS_PAUSED -> "Paused"
                DownloadManager.STATUS_PENDING -> "Pending"
                DownloadManager.STATUS_RUNNING -> "Downloading..."
                DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                    url.lastIndexOf("/") + 1
                )
                else -> "There's nothing to download"
            }
            return msg
        }
    }
}