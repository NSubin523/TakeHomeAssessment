package com.example.assesment.screens.displayImages

import RuntimePermissionHelper
import RuntimePermissionHelper.Companion.PERMISSION_READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assesment.R
import com.example.assesment.core.ResultState
import com.example.assesment.modules.DownloadHelper
import com.example.assesment.screens.displayImages.adapter.ImageAdapter
import com.example.assesment.screens.displayImages.viewModel.ImagesViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var runtimePermissionHelper: RuntimePermissionHelper
    private lateinit var currentID: String
    private lateinit var imageContext: ActivityResultLauncher<String>

    private val model: ImagesViewModel by viewModels()
    private val adapter: ImageAdapter by lazy {
        ImageAdapter(baseContext,
            onDownload = { url -> onDownload(url) },
            onUpload = { id -> onUpload(id) },
            onFilter = { url -> onFilter(url) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageRecyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        imageRecyclerView.layoutManager = linearLayoutManager
        imageRecyclerView.adapter = adapter
        model.images.observe(this, {
            adapter.setImages(it)
        })
        imageContext =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { model.uploadImage(uri, baseContext, currentID) }
            }
        model.uploadState.observe(this, {
            when (it) {
                is ResultState.Error -> {
                    Toast.makeText(baseContext, it.message, Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
                ResultState.Loading -> showLoading()
                ResultState.Success -> {
                    hideLoading()
                    Toast.makeText(baseContext, "Image sent to Eulerity", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onDownload(url: String) {
        val downloadHelper = DownloadHelper()
        downloadHelper.saveImage(url, baseContext)
    }

    private fun onUpload(id: String) {
        this.currentID = id
        runtimePermissionHelper = RuntimePermissionHelper.getInstance(this)
        if (runtimePermissionHelper.isPermissionAvailable(PERMISSION_READ_EXTERNAL_STORAGE)) {
            openImagePicker()
        } else {
            runtimePermissionHelper.setActivity(this)
            runtimePermissionHelper.requestPermissionsIfDenied(PERMISSION_READ_EXTERNAL_STORAGE)
        }
    }

    private fun onFilter(url: String) {
        val intent = Intent(baseContext, ApplyFilters::class.java)
        intent.putExtra("image_url", url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun openImagePicker() {
        imageContext.launch("image/*")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in grantResults) {
            if (i == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                runtimePermissionHelper.requestPermissionsIfDenied(PERMISSION_READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }
}